/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.doctor

import android.util.Log
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
class DoctorAppointmentsViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _doctorAppointmentsList = MutableStateFlow<List<Appointment?>>(mutableListOf())
    val doctorAppointmentsList = _doctorAppointmentsList.asStateFlow()

    init {
        getDoctorAppointments()
    }

    private fun getDoctorAppointments() {
        viewModelScope.launch {
            val doctorEmail = auth.currentUser!!.email.toString()

            appointmentRepository.getDoctorAppointments(doctorEmail).fold(
                onSuccess = { list ->
                    _doctorAppointmentsList.update { list }

                    Log.d("TAMAÑO_DOCTOR", _doctorAppointmentsList.value.size.toString())
                },
                onFailure = { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }

}