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
import com.example.mitfg.domain.model.Appointment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentReservationViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _appointmentList = MutableStateFlow<List<Appointment?>>(emptyList())
    val appointmentList = _appointmentList.asStateFlow()

    init {
        getAppointmentsOfUser()
    }

    fun getAppointmentsOfUser() {
        viewModelScope.launch {
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



}