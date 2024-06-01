/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
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
import com.example.mitfg.ui.main.AlarmReceiver
import com.example.mitfg.ui.main.MainActivity
import com.example.mitfg.ui.main.RebootReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class FrequencyFragment : Fragment(R.layout.fragment_frequence), AdapterView.OnItemSelectedListener, TextToSpeech.OnInitListener {

    private var _binding : FragmentFrequenceBinding? = null
    private val binding get() = _binding
    private val viewModel: AlarmCreationViewModel by activityViewModels()

    private lateinit var textToSpeech: TextToSpeech

    private fun playFrequencySelectionAudioMessage() {
        textToSpeech.speak(getString(R.string.frequencySelectionVoiceMessage), TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFrequenceBinding.bind(view)

        setUpFrequencySpinner()
        setUpButtonsListeners()

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
                val intent = Intent(context, AlarmReceiver::class.java)

                intent.putExtra("idAlarm", viewModel.alarm.value.id)
                intent.putExtra("medicineName", viewModel.alarm.value.medicineName)

                var medicinePresentation = ""

                when (viewModel.getMedicinePresentation()) {
                    requireContext().getString(R.string.pillAbrev) -> medicinePresentation = requireContext().getString(R.string.showPill)
                    requireContext().getString(R.string.packetAbrev) -> medicinePresentation = requireContext().getString(R.string.showPacket)
                    requireContext().getString(R.string.MlAbrev) -> medicinePresentation = requireContext().getString(R.string.showMl)
                    else -> ""
                }

                intent.putExtra("medicinePresentation", medicinePresentation)
                intent.putExtra("dosage", viewModel.alarm.value.quantity)

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    viewModel.alarm.value.id.toInt(),
                    intent,
                    PendingIntent.FLAG_MUTABLE
                )

                val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, viewModel.alarm.value.hourStart)
                    set(Calendar.MINUTE, viewModel.alarm.value.minuteStart)
                }

                val receiver = ComponentName(requireContext(), RebootReceiver::class.java)

                requireContext().packageManager.setComponentEnabledSetting(
                    receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )

                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    // viewModel.alarm.value.frequency,
                    pendingIntent
                )

                Toast.makeText(context, getString(R.string.alarmCreated), Toast.LENGTH_SHORT).show()

                playAlarmCreationSuccessAudioMessage()

                finishActivity()
            }
        }
    }

    private fun playAlarmCreationSuccessAudioMessage() {
        textToSpeech.speak(getString(R.string.alarmCreated), TextToSpeech.QUEUE_FLUSH, null, null)
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

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = setVoiceLanguage()

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(ContentValues.TAG, "Language not supported")
            } else {
                playFrequencySelectionAudioMessage()
            }
        } else {
            Log.e(ContentValues.TAG, "Initialization failed")
        }
    }

}