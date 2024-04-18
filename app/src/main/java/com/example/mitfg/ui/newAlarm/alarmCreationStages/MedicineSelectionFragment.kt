package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.content.ContentValues
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentMedicineSelectionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MedicineSelectionFragment : Fragment(R.layout.fragment_medicine_selection), TextToSpeech.OnInitListener {

    private var _binding : FragmentMedicineSelectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlarmCreationViewModel by activityViewModels()

    private lateinit var textToSpeech: TextToSpeech

    private fun playSelectMedicineAudioMessage() {
        textToSpeech.speak(getString(R.string.medicineSelectionVoiceMessage), TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMedicineSelectionBinding.bind(view)

        val callback = object : MedicineListAdapter.ItemClicked {
            override fun onClick(medicineName: String) {
                viewModel.assignMedicineName(medicineName)
                viewModel.increaseProgressBar()

                navigateToNextScene()
            }
        }
        val adapter = MedicineListAdapter(callback)
        binding.recyclerViewMedicines.adapter = adapter

        binding.buttonBefore.setOnClickListener {
            finishAlarmActivity()
        }

        lifecycleScope.launch {
            viewModel.medicinesList.collect { list ->
                adapter.submitList(list)
            }
        }

        textToSpeech = TextToSpeech(requireContext(), this)
    }

    private fun setVoiceLanguage() : Int {
        val locale = Locale.getDefault().displayLanguage

        val result = when (locale) {
            "English" -> textToSpeech.setLanguage(Locale("en", "US"))
            "Spanish" -> textToSpeech.setLanguage(Locale("es", "ES"))
            "Catalan" -> textToSpeech.setLanguage(Locale("ca", "ES"))
            else -> textToSpeech.setLanguage(Locale("es", "ES"))
        }

        return result
    }

    private fun finishAlarmActivity() {
        requireActivity().finish()
    }

    private fun navigateToNextScene() {
        findNavController().navigate(R.id.medicationPresentationFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = setVoiceLanguage()

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(ContentValues.TAG, "Language not supported")
            } else {
                playSelectMedicineAudioMessage()
            }
        } else {
            Log.e(ContentValues.TAG, "Initialization failed")
        }
    }

}