package com.example.mitfg.ui.appointmentReservation

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val viewModel : DateTimeViewModel by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.datePicker.setOnDateChangedListener { view, yearListener, monthOfYear, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, monthOfYear, dayOfMonth)
            val dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK)
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val rightMonth = month + 1

        viewModel.assignDate(year, rightMonth, dayOfMonth)

        val newFragment = TimePickerFragment()
        newFragment.show(parentFragmentManager, "timePicker")
    }


}