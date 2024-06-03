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

/**
 * The appointment reservation fragment's view model.
 * It contains every data that the fragment handles.
 */
@HiltViewModel
class AppointmentReservationViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    // MutableStateFlow to hold the list of appointments
    private val _appointmentList = MutableStateFlow<List<Appointment?>>(emptyList())
    val appointmentList = _appointmentList.asStateFlow()

    // Fetch appointments of the current user upon ViewModel creation
    init {
        getAppointmentsOfUser()
    }

    /**
     * Fetches appointments of the current user from the repository.
     */
    private fun getAppointmentsOfUser() {
        viewModelScope.launch {
            // Get the current user's email
            val email = auth.currentUser!!.email.toString()

            // Fetch appointments from the repository
            appointmentRepository.getAppointmentsOfUser(email).fold(
                // On success, update the appointmentList StateFlow
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