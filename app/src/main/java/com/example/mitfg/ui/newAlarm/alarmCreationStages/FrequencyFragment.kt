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

@AndroidEntryPoint
class FrequencyFragment : Fragment(R.layout.fragment_frequence), AdapterView.OnItemSelectedListener {

    private var _binding : FragmentFrequenceBinding? = null
    private val binding get() = _binding
    private val viewModel: AlarmCreationViewModel by activityViewModels()

    @Inject
    lateinit var voiceMessagePlayer: TextToSpeechHelper

    @Inject
    lateinit var alarmManagerHelper: AlarmManagerHelper

    private fun playFrequencySelectionAudioMessage() {
        voiceMessagePlayer.speak(getString(R.string.frequencySelectionVoiceMessage))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFrequenceBinding.bind(view)

        setUpFrequencySpinner()
        setUpButtonsListeners()

        playFrequencySelectionAudioMessage()
    }

    private fun setUpButtonsListeners() {
        binding!!.buttonNext.setOnClickListener {
            completeAlarmCreation()
        }
        binding!!.buttonBefore.setOnClickListener {
            decreaseProgressBar()

            turnBackToLastScreen()
        }
    }

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

    private fun decreaseProgressBar() {
        viewModel.decreaseProgressBar()
    }

    private fun assignHourAndMinuteStart() {
        val hour = binding!!.timePickerFrequence.hour
        val minute = binding!!.timePickerFrequence.minute

        viewModel.assignHourStart(hour)
        viewModel.assignMinuteStart(minute)
    }

    private fun turnBackToLastScreen() {
        findNavController().navigate(R.id.dosageSelectionFragment)
    }

    private fun assignFrequencyInstruction() {
        val frequency = binding!!.frequenceSpinner.selectedItem.toString()
        viewModel.assignFrequencyInstruction(frequency)
    }

    private fun completeAlarmCreation() {
        assignFrequencyInstruction()
        assignHourAndMinuteStart()

        lifecycleScope.launch {
            addAlarm()
        }


        launchMainActivity()
    }

    private fun launchMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }

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

    private fun playAlarmCreationSuccessAudioMessage() {
        voiceMessagePlayer.speak(getString(R.string.alarmCreated))
    }

    private fun finishActivity() {
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.assignFrequencyInstruction(parent?.getItemAtPosition(position).toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}