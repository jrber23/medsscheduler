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

/**
 * The fragment where the alarm medicine is selected
 * The fragment where the alarm medicine is selected
 */
@AndroidEntryPoint
class MedicineSelectionFragment : Fragment(R.layout.fragment_medicine_selection) {

    // The binding that contains every reference to all the UI elements
    private var _binding : FragmentMedicineSelectionBinding? = null
    private val binding get() = _binding!!

    // The activity's ViewModel instance
    private val viewModel: AlarmCreationViewModel by activityViewModels()

    // The TextToSpeechHelper instance
    @Inject
    lateinit var voiceMessagePlayer: TextToSpeechHelper

    /**
     * Plays the message that guides the user to select a medicine
     */
    private fun playSelectMedicineAudioMessage() {
        voiceMessagePlayer.speak(getString(R.string.medicineSelectionVoiceMessage))
    }

    /**
     * This method launches when the fragment is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMedicineSelectionBinding.bind(view)

        // Creates the callback to execute when an item is selected
        val callback = object : MedicineListAdapter.ItemClicked {
            override fun onClick(medicineName: String) {
                viewModel.assignMedicineName(medicineName)
                viewModel.increaseProgressBar()

                navigateToNextScene()
            }
        }

        // Creates the adapter and attaches it to the recycler view
        val adapter = MedicineListAdapter(callback)
        binding.recyclerViewMedicines.adapter = adapter

        // Closes the alarm activity when the before button is pressed
        binding.buttonBefore.setOnClickListener {
            finishAlarmActivity()
        }

        // Every time a new list value is received, the recycler view is updated.
        lifecycleScope.launch {
            viewModel.medicinesList.collect { list ->
                adapter.submitList(list)
            }
        }

        playSelectMedicineAudioMessage()
    }

    /**
     * Finishes the alarm creation activity
     */
    private fun finishAlarmActivity() {
        requireActivity().finish()
    }

    /**
     * Navigates to the next alarm creation stage
     */
    private fun navigateToNextScene() {
        findNavController().navigate(R.id.medicationPresentationFragment)
    }

    /**
     * Every triggered action when the fragment is destroyed
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}