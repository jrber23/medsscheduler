/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitfg.data.healthAdvices.HealthAdviceRepository
import com.example.mitfg.data.instructionAlarm.AlarmRepository
import com.example.mitfg.data.users.UserRepository
import com.example.mitfg.domain.model.HealthAdvice
import com.example.mitfg.domain.model.User
import com.example.mitfg.utils.FirebaseTranslator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The main activity's view model. It contains every data shown by the activity
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val healthAdviceRepository: HealthAdviceRepository,
    private val userRepository: UserRepository,
    auth: FirebaseAuth,
    private val translator: FirebaseTranslator,
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    // State that holds the health advice shown in the activity
    private val _healthAdvice = MutableStateFlow<HealthAdvice?>(null)
    val healthAdvice = _healthAdvice.asStateFlow()

    // State that holds the current user information
    private val _user = MutableStateFlow<User>(User())
    val user = _user.asStateFlow()

    init {
        // Get the current user's email
        val userEmail = auth.currentUser?.email.toString()

        // Load the health advice and user information
        getHealthAdvice()
        getUserByEmail(userEmail)
    }

    /**
     * Gets a random health advice from the repository
     */
    private fun getHealthAdvice() {
        viewModelScope.launch {
            healthAdviceRepository.getRandomHealthAdvice().fold(
                onSuccess = { advice ->
                    // Translate the health advice
                    translateHealthAdvice(advice)

                    // Update the state with the new health advice
                    _healthAdvice.update { advice }
                },
                onFailure = { throwable -> Log.d("FAILURE", throwable.toString()) }
            )
        }
    }

    /**
     * Retrieves the user with the given email requesting to the user repository
     * @param email The email of the requested user
     */
    private fun getUserByEmail(email: String) {
        viewModelScope.launch {
            userRepository.getUserByEmail(email).fold(
                onSuccess = { foundUser ->
                    // Update the state with the found user information
                    _user.update { foundUser!! }
                },
                onFailure =  { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }

    /**
     * Checks if the retrieved user is a doctor.
     * @return true if the user is a doctor and false otherwise
     */
    fun userIsDoctor() : Boolean {
        return this.user.value.isDoctor
    }

    /**
     * Translate the retrieved health advice into the current device language.
     * @param advice the retrieved health advice to be translated
     */
    private fun translateHealthAdvice(advice: HealthAdvice?) {
        viewModelScope.launch {
            // Translate the title of the health advice
            translator.translate(advice!!.title).fold(
                onSuccess = {
                    // Update the state with the translated title
                    _healthAdvice.update { advice ->
                        advice?.copy(title = it)
                    }
                },
                onFailure = { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )

            translator.translate(advice.description).fold(
                onSuccess = {
                    // Update the state with the translated description
                    _healthAdvice.update { advice ->
                        advice?.copy(description = it)
                    }
                },
                onFailure = { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }

    /**
     * Add a taken dosage to an alarm
     * @param idAlarm the ID of the alarm to add a new taken dosage
     */
    fun addTakenDosage(idAlarm: Long) {
        viewModelScope.launch {
            alarmRepository.addTakenDosageToAlarm(idAlarm)
        }
    }
}