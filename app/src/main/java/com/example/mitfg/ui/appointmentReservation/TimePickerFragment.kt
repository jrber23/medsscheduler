/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.appointmentReservation

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.mitfg.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * The fragment where the appointment exact hour is selected
 */
@AndroidEntryPoint
class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    // ViewModel injection
    private val viewModel: DateTimeViewModel by activityViewModels()

    /**
     * Called to create the TimePickerDialog.
     * @return A new TimePickerDialog instance
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Create and return a new TimePickerDialog instance
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    /**
     * Called when the user sets the time in the TimePickerDialog.
     * @param view The TimePicker view
     * @param hourOfDay The selected hour
     * @param minute The selected minute
     */
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        // Assign selected hour and minute and add the appointment
        lifecycleScope.launch {
            viewModel.assignHourAndMinute(hourOfDay, minute)

            viewModel.checkAppointment()

            if (!viewModel.existsAppointment.value!!) {
                viewModel.addAppointment()
            } else {
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

                builder
                    .setTitle(R.string.existing_appointment_title)
                    .setMessage(R.string.existing_appointment_message)
                    .setPositiveButton(android.R.string.ok) { dialog, which ->
                        dismiss()
                    }

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

    }



}