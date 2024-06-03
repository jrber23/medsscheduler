/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.appointmentReservation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitfg.data.appointment.AppointmentRepository
import com.example.mitfg.data.users.UserRepository
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
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    // State flow variable for appointment list
    private val _appointmentList = MutableStateFlow<List<Appointment?>>(mutableListOf())
    val appointmentList = _appointmentList.asStateFlow()

    // State flow variable for a new appointment
    private val _newAppointment = MutableStateFlow(Appointment(auth.currentUser!!.email.toString(), "", 0,0,0,0,0))
    val newAppointment = _newAppointment.asStateFlow()

    init {
        getAppointmentsOfUser()
        getPatientDoctorByEmail()
    }

    /**
     * Assign hour and minute to the new appointment
     * @param hourOfDay the new appointment's hour
     * @param minute the new appointment's minute
     */
    fun assignHourAndMinute(hourOfDay: Int, minute: Int) {
        _newAppointment.update { appointment ->
            appointment.copy(hour = hourOfDay, minute = minute)
        }
    }

    /**
     * Assign date to the new appointment
     * @param year the new appointment's year
     * @param month the new appointment's month
     * @param day the new appointment's day
     */
    fun assignDate(year: Int, month: Int, day: Int) {
        _newAppointment.update { appointment ->
            appointment.copy(day = day, month = month, year = year)
        }
    }

    /**
     * Add the new appointment to the repository
     */
    fun addAppointment() {
        viewModelScope.launch {
            appointmentRepository.addNewAppointment(_newAppointment.value)

            // Update the appointment list
            getAppointmentsOfUser()
        }
    }

    /**
     * Get appointments associated with the current user
     */
    private fun getAppointmentsOfUser() {
        viewModelScope.launch {
            // The current user email
            val email = auth.currentUser!!.email.toString()

            appointmentRepository.getAppointmentsOfUser(email).fold(
                onSuccess = { list ->
                    _appointmentList.update { list }
                },
                onFailure = { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }

    /**
     * Get the doctor email associated with the current user
     */
    private fun getPatientDoctorByEmail() {
        viewModelScope.launch {
            // The current user email
            val email = auth.currentUser!!.email.toString()

            userRepository.getPatientDoctorByEmail(email).fold(
                onSuccess = { doctorEmail ->
                    _newAppointment.update { appointment ->
                        appointment.copy(emailDoctor = doctorEmail)
                    }
                },
                onFailure = { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }


}