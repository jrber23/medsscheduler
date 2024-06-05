/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentFrequenceBinding
import com.example.mitfg.ui.main.MainActivity
import com.example.mitfg.utils.AlarmManagerHelper
import com.example.mitfg.utils.TextToSpeechHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The fragment alarm frequency selection.
 * It's here where the user selects the alarm frequency.
 */
@AndroidEntryPoint
class FrequencyFragment : Fragment(R.layout.fragment_frequence), AdapterView.OnItemSelectedListener {

    // The binding that contains every reference to all the UI elements
    private var _binding : FragmentFrequenceBinding? = null
    private val binding get() = _binding

    // The fragment's ViewModel instance
    private val viewModel: AlarmCreationViewModel by activityViewModels()

    // The TextToSpeechHelper instance
    @Inject
    lateinit var voiceMessagePlayer: TextToSpeechHelper

    // The AlarmManagerHelper instance
    @Inject
    lateinit var alarmManagerHelper: AlarmManagerHelper

    /**
     * Plays the message that guides the user to select a frequency
     */
    private fun playFrequencySelectionAudioMessage() {
        voiceMessagePlayer.speak(getString(R.string.frequencySelectionVoiceMessage))
    }

    /**
     * This method launches when the fragment's view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFrequenceBinding.bind(view)

        setUpFrequencySpinner()
        setUpButtonsListeners()

        playFrequencySelectionAudioMessage()
    }

    /**
     * Sets up the click listeners for the buttons
     */
    private fun setUpButtonsListeners() {
        binding!!.buttonNext.setOnClickListener {
            completeAlarmCreation()
        }
        binding!!.buttonBefore.setOnClickListener {
            decreaseProgressBar()

            turnBackToLastScreen()
        }
    }

    /**
     * Sets up the frequency spinner
     */
    private fun setUpFrequencySpinner() {
        val spinner: Spinner = binding!!.frequenceSpinner
        ArrayAdapter.createFromResource(
            requireActivity().baseContext,
            R.array.frequence_array,
            R.layout.simple_spinner_item_custom
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = arrayAdapter
        }
        spinner.onItemSelectedListener = this

        binding!!.timePickerFrequence.setIs24HourView(true)
        binding!!.timePickerFrequence.setOnClickListener {
            assignHourAndMinuteStart()
        }
    }

    /**
     * Decreases the progress bar value
     */
    private fun decreaseProgressBar() {
        viewModel.decreaseProgressBar()
    }

    /**
     * Assigns the start hour and minute from the TimePicker to the ViewModel
     */
    private fun assignHourAndMinuteStart() {
        val hour = binding!!.timePickerFrequence.hour
        val minute = binding!!.timePickerFrequence.minute

        viewModel.assignHourStart(hour)
        viewModel.assignMinuteStart(minute)
    }

    /**
     * Navigates to the previous fragment
     */
    private fun turnBackToLastScreen() {
        findNavController().navigate(R.id.dosageSelectionFragment)
    }

    /**
     * Assigns the selected frequency instruction to the ViewModel
     */
    private fun assignFrequencyInstruction() {
        val frequency = binding!!.frequenceSpinner.selectedItem.toString()
        viewModel.assignFrequencyInstruction(frequency)
    }

    /**
     * Completes the alarm creation process
     */
    private fun completeAlarmCreation() {
        assignFrequencyInstruction()
        assignHourAndMinuteStart()

        lifecycleScope.launch {
            addAlarm()
        }


        launchMainActivity()
    }

    /**
     * Launches the main activity
     */
    private fun launchMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }

    /**
     * Adds the alarm using the ViewModel and AlarmManagerHelper
     */
    private suspend fun addAlarm() {
        viewModel.addAlarm()

        viewModel.alarmIsCompleted.collect { isCompleted ->
            if (isCompleted) {
                val alarm = viewModel.alarm.value

                alarmManagerHelper.createAlarm(alarm)

                Toast.makeText(context, getString(R.string.alarmCreated), Toast.LENGTH_SHORT).show()

                playAlarmCreationSuccessAudioMessage()

                finishActivity()
            }
        }
    }

    /**
     * Plays an audio message indicating that the alarm was successfully created
     */
    private fun playAlarmCreationSuccessAudioMessage() {
        voiceMessagePlayer.speak(getString(R.string.alarmCreated))
    }

    /**
     * Finishes the current activity
     */
    private fun finishActivity() {
        requireActivity().finish()
    }

    /**
     * Cleans up the binding when the view is destroyed
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Handles item selection in the spinner
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.assignFrequencyInstruction(parent?.getItemAtPosition(position).toString())
    }

    /**
     * Handles the case when no item is selected in the spinner
     */
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}