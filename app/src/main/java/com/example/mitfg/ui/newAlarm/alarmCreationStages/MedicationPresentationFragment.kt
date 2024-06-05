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
import androidx.navigation.fragment.findNavController
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentMedicationPresentationBinding
import com.example.mitfg.utils.TextToSpeechHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * The fragment medicine presentation selection.
 * It's here where the user selects the medicine presentation of the alarm.
 */
@AndroidEntryPoint
class MedicationPresentationFragment : Fragment(R.layout.fragment_medication_presentation) {

    // The binding that contains every reference to all the UI elements
    private var _binding : FragmentMedicationPresentationBinding? = null
    private val binding get() = _binding!!

    // The fragment's ViewModel instance
    private val viewModel: AlarmCreationViewModel by activityViewModels()

    // The TextToSpeechHelper instance
    @Inject
    lateinit var voiceMessageHelper: TextToSpeechHelper

    /**
     *  Plays the message that guides the user to select a medicine presentation
     */
    private fun playSelectMedicinePresentationAudioMessage() {
        voiceMessageHelper.speak(getString(R.string.medicinePresentationVoiceMessage))
    }

    /**
     * This method launches when the fragment's view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentMedicationPresentationBinding.bind(view)

        // Callback for handling item clicks in the medicine presentation list
        val callback = object : MedicinePresentationListAdapter.ItemClicked {
            override fun onClick(presentation: String) {
                viewModel.assignMedicinePresentation(presentation)
                viewModel.increaseProgressBar()

                navigateToNextScene()
            }
        }

        // List of medicine presentation options
        val items = listOf<String>(
            getString(R.string.tvPill),
            getString(R.string.tvPacket),
            getString(R.string.tvMililitres)
        )

        // Setting up the adapter for the recycler view
        val adapter = MedicinePresentationListAdapter(callback)
        binding.medicinePresentationRecyclerView.adapter = adapter

        // Submit the list of items to the adapter
        adapter.submitList(items)

        // Set click listeners for the "Next" and "Before" buttons
        binding.buttonNext.setOnClickListener {
            viewModel.increaseProgressBar()

            navigateToNextScene()
        }

        binding.buttonBefore.setOnClickListener {
            viewModel.decreaseProgressBar()

            navigateBack()
        }

        // Play the audio message for selecting medicine presentation
        playSelectMedicinePresentationAudioMessage()
    }

    /**
     * Navigates to the previous fragment
     */
    private fun navigateBack() {
        findNavController().navigate(R.id.medicineSelectionFragment)
    }

    /**
     * Navigates to the dosage selection fragment
     */
    private fun navigateToNextScene() {
        findNavController().navigate(R.id.dosageSelectionFragment)
    }

    /**
     * Cleans up the binding when the view is destroyed
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}