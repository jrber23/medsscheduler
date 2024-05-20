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

@HiltViewModel
class DoctorSelectionViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _doctorList = MutableStateFlow<List<User?>>(emptyList())
    val doctorList : StateFlow<List<User?>> = _doctorList.asStateFlow()

    init {
        getAllDoctors()
    }

    fun getAllDoctors() {
        viewModelScope.launch {
            userRepository.getAllDoctors().fold(
                onSuccess = { list ->
                    _doctorList.update { list }
                },
                onFailure = { throwable ->
                    Log.d("EXCEPTION", throwable.toString())
                }
            )
        }
    }

    fun updateDoctor(email: String) {
        viewModelScope.launch {
            val user = auth.currentUser!!.email.toString()

            userRepository.updateDoctor(email, user)
        }
    }


}