/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentMedicineSelectionBinding
import com.example.mitfg.utils.TextToSpeechHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MedicineSelectionFragment : Fragment(R.layout.fragment_medicine_selection) {

    private var _binding : FragmentMedicineSelectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlarmCreationViewModel by activityViewModels()

    @Inject
    lateinit var voiceMessagePlayer: TextToSpeechHelper

    private fun playSelectMedicineAudioMessage() {
        voiceMessagePlayer.speak(getString(R.string.medicineSelectionVoiceMessage))
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

        playSelectMedicineAudioMessage()
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

}