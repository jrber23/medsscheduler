/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitfg.data.users.UserRepository
import com.example.mitfg.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The Doctor Selection's view model that handles every data shown by the activity.
 */
@HiltViewModel
class DoctorSelectionViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    // StateFlow to hold the list of doctors
    private val _doctorList = MutableStateFlow<List<User?>>(emptyList())
    val doctorList : StateFlow<List<User?>> = _doctorList.asStateFlow()

    // Fetch the list of all doctors when the ViewModel is initialized
    init {
        getAllDoctors()
    }

    /**
     * Fetches the list of all doctors from the repository and updates the StateFlow.
     */
    private fun getAllDoctors() {
        viewModelScope.launch {
            userRepository.getAllDoctors().fold(
                onSuccess = { list ->
                    // Update the _doctorList StateFlow with the fetched list of doctors
                    _doctorList.update { list }
                },
                onFailure = { throwable ->
                    Log.d("EXCEPTION", throwable.toString())
                }
            )
        }
    }

    /**
     * Updates the associated doctor for the current user.
     *
     * @param email The email of the selected doctor.
     */
    fun updateDoctor(email: String) {
        viewModelScope.launch {
            // Get the current user's email
            val user = auth.currentUser!!.email.toString()

            userRepository.updateDoctor(email, user)
        }
    }


}