package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentFrequenceBinding
import com.example.mitfg.ui.main.AlarmReceiver
import com.example.mitfg.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class FrequencyFragment : Fragment(R.layout.fragment_frequence), AdapterView.OnItemSelectedListener {

    private var _binding : FragmentFrequenceBinding? = null
    private val binding get() = _binding
    private val viewModel: AlarmCreationViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFrequenceBinding.bind(view)

        setUpFrequencySpinner()
        setUpButtonsListeners()

        setUpObservers()
    }

    private fun setUpObservers() {
        /* viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.alarmIsCompleted.collect { isCompleted ->
                    if (isCompleted) {
                        val intent = Intent(requireContext(), AlarmReceiver::class.java)
                        intent.putExtra("idAlarm", viewModel.alarm.value.id)
                        intent.putExtra("medicineName", viewModel.alarm.value.medicineName)
                        intent.putExtra("dosage", viewModel.alarm.value.quantity)

                        val pendingIntent = PendingIntent.getBroadcast(
                            requireContext(),
                            viewModel.alarm.value.id.toInt(),
                            intent,
                            PendingIntent.FLAG_MUTABLE
                        )

                        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

                        val calendar = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, viewModel.alarm.value.hourStart)
                            set(Calendar.MINUTE, viewModel.alarm.value.minuteStart)
                        }

                        alarmManager.setInexactRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            viewModel.alarm.value.frequency,
                            pendingIntent
                        )
                    }
                }
            }
        } */
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
            android.R.layout.simple_spinner_item
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

        addAlarm()

        launchMainActivity()
    }

    private fun launchMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }

    private fun addAlarm() {
        viewModel.addAlarm()

        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.putExtra("idAlarm", viewModel.alarm.value.id)
        intent.putExtra("medicineName", viewModel.alarm.value.medicineName)
        intent.putExtra("medicinePresentation", viewModel.alarm.value.medicinePresentation)
        intent.putExtra("dosage", viewModel.alarm.value.quantity)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            viewModel.alarm.value.id.toInt(),
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, viewModel.alarm.value.hourStart)
            set(Calendar.MINUTE, viewModel.alarm.value.minuteStart)
        }

        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            calendar.timeInMillis,
            60*1000,
            pendingIntent
        )
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