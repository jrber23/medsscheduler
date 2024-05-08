package com.example.mitfg.ui.appointmentReservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitfg.data.appointment.AppointmentRepository
import com.example.mitfg.domain.model.Appointment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DateTimeViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _newAppointment = MutableStateFlow(Appointment(auth.currentUser!!.email.toString(), 0,0,0,0,0))
    val newAppointment = _newAppointment.asStateFlow()

    fun assignHourAndMinute(hourOfDay: Int, minute: Int) {
        _newAppointment.update { appointment ->
            appointment.copy(hour = hourOfDay, minute = minute)
        }
    }

    fun assignDate(year: Int, month: Int, day: Int) {
        _newAppointment.update { appointment ->
            appointment.copy(day = day, month = month, year = year)
        }
    }

    fun addAppointment() {
        viewModelScope.launch {
            appointmentRepository.addNewAppointment(_newAppointment.value)
        }
    }


}