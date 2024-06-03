/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.appointmentReservation

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint

/**
 * The fragment where the appointment date is selected
 */
@AndroidEntryPoint
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    // ViewModel injection
    private val viewModel : DateTimeViewModel by activityViewModels()

    /**
     * Creates and configures the DatePickerDialog.
     * @param savedInstanceState The saved instance state
     * @return The configured DatePickerDialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create the DatePickerDialog
        val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)

        // Set the minimum date to tomorrow
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        // Listen for date changes
        datePickerDialog.datePicker.setOnDateChangedListener { view, yearListener, monthOfYear, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, monthOfYear, dayOfMonth)
            val dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK)

            // If selected date is a weekend, adjust to the next weekday
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                if (dayOfWeek == Calendar.SATURDAY) {
                    selectedCalendar.add(Calendar.DAY_OF_MONTH, 2)
                } else {
                    selectedCalendar.add(Calendar.DAY_OF_MONTH, 1)
                }
                datePickerDialog.datePicker.updateDate(
                    selectedCalendar.get(Calendar.YEAR),
                    selectedCalendar.get(Calendar.MONTH),
                    selectedCalendar.get(Calendar.DAY_OF_MONTH)
                )
            }
        }

        return datePickerDialog
    }

    /**
     * Called when a date is set in the DatePickerDialog.
     * @param view The date picker view
     * @param year The selected year
     * @param month The selected month
     * @param dayOfMonth The selected day of month
     */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Adjust month value as it's zero-based in DatePickerDialog
        val rightMonth = month + 1

        // Pass selected date to the view model
        viewModel.assignDate(year, rightMonth, dayOfMonth)

        // Show TimePickerFragment
        val newFragment = TimePickerFragment()
        newFragment.show(parentFragmentManager, "timePicker")
    }


}