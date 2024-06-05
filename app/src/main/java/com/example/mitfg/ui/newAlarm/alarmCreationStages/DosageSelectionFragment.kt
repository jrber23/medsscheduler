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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentDosageSelectionBinding
import com.example.mitfg.utils.TextToSpeechHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * The fragment dosage selection.
 * It's here where the user selects the alarm dosage.
 */
@AndroidEntryPoint
class DosageSelectionFragment : Fragment(R.layout.fragment_dosage_selection) {

    // The binding that contains every reference to all the UI elements
    private var _binding : FragmentDosageSelectionBinding? = null
    private val binding get() = _binding!!

    // The fragment's ViewModel instance
    private val viewModel: AlarmCreationViewModel by activityViewModels()

    // The TextToSpeechHelper instance
    @Inject
    lateinit var voiceMessagePlayer: TextToSpeechHelper

    /**
     * Plays the message that guides the user to select a dosage
     */
    private fun playDosageSelectionAudioMessage() {
        voiceMessagePlayer.speak(getString(R.string.dosageSelectionVoiceMessage))
    }

    /**
     * This method launches when the fragment is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDosageSelectionBinding.bind(view)

        // Get a reference to the spinner from the binding
        val spinner: Spinner = binding.pillQuantitySpinner

        // Create an OnItemSelectedListener for the spinner
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.assignDosageQuantity(parent?.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        // Get the medicine presentation from the ViewModel
        val medicinePresentation = viewModel.getMedicinePresentation()

        // Set the appropriate array adapter based on the medicine presentation
        when (medicinePresentation) {
            getString(R.string.pillAbrev) -> {
                setQuantityArrayAdapter(getString(R.string.pill), R.array.pill_quantity_array, spinner)
            }

            getString(R.string.packetAbrev) -> {
                setQuantityArrayAdapter(getString(R.string.packet), R.array.packet_quantity_array, spinner)
            }

            getString(R.string.MlAbrev) -> {
                setQuantityArrayAdapter(getString(R.string.mililitres), R.array.mililiters_quantity_array, spinner)
            }
        }

        // Assign the listener to the spinner
        spinner.onItemSelectedListener = listener

        // Set click listeners for the "Next" and "Before" buttons
        binding.buttonNext.setOnClickListener {
            viewModel.increaseProgressBar()

            navigateForward()
        }

        binding.buttonBefore.setOnClickListener {
            viewModel.decreaseProgressBar()

            navigateBack()
        }

        // Play the audio message for dosage selection
        playDosageSelectionAudioMessage()
    }

    /**
     * Sets the adapter for the quantity spinner based on the medicine presentation
     */
    private fun setQuantityArrayAdapter(medicinePresentation: String, array: Int, spinner: Spinner) {
        binding.tvDosage.text = medicinePresentation

        ArrayAdapter.createFromResource(
            requireActivity().baseContext,
            array,
            R.layout.simple_spinner_item_custom
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = arrayAdapter
        }
    }

    /**
     * Assigns the selected pill quantity to the ViewModel
     */
    private fun assignPillQuantity() {
        viewModel.assignDosageQuantity(binding.pillQuantitySpinner.selectedItem.toString())
    }

    /**
     * Navigates to the next fragment and assigns the selected pill quantity
     */
    private fun navigateForward() {
        assignPillQuantity()

        findNavController().navigate(R.id.frequenceFragment)
    }

    /**
     * Navigates to the previous fragment
     */
    private fun navigateBack() {
        findNavController().navigate(R.id.medicationPresentationFragment)
    }

    /**
     * Cleans up the binding when the view is destroyed
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}