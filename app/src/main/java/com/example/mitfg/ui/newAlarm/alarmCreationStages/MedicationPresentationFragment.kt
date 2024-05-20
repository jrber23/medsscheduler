/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.content.ContentValues
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentMedicationPresentationBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MedicationPresentationFragment : Fragment(R.layout.fragment_medication_presentation), TextToSpeech.OnInitListener {

    private var _binding : FragmentMedicationPresentationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlarmCreationViewModel by activityViewModels()

    private lateinit var textToSpeech: TextToSpeech

    private fun playSelectMedicinePresentationAudioMessage() {
        textToSpeech.speak(getString(R.string.medicinePresentationVoiceMessage), TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentMedicationPresentationBinding.bind(view)

        val callback = object : MedicinePresentationListAdapter.ItemClicked {
            override fun onClick(presentation: String) {
                viewModel.assignMedicinePresentation(presentation)
                viewModel.increaseProgressBar()

                navigateToNextScene()
            }
        }

        val items = listOf<String>(
            getString(R.string.tvPill),
            getString(R.string.tvPacket),
            getString(R.string.tvMililitres)
        )

        val adapter = MedicinePresentationListAdapter(callback)
        binding.medicinePresentationRecyclerView.adapter = adapter

        adapter.submitList(items)

        binding.buttonNext.setOnClickListener {
            viewModel.increaseProgressBar()

            navigateToNextScene()
        }

        binding.buttonBefore.setOnClickListener {
            viewModel.decreaseProgressBar()

            navigateBack()
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

    private fun navigateBack() {
        findNavController().navigate(R.id.medicineSelectionFragment)
    }

    private fun navigateToNextScene() {
        findNavController().navigate(R.id.dosageSelectionFragment)
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
                playSelectMedicinePresentationAudioMessage()
            }
        } else {
            Log.e(ContentValues.TAG, "Initialization failed")
        }
    }

}