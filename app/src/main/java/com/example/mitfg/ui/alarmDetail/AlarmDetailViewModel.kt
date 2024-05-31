/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.alarmDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitfg.data.instructionAlarm.AlarmRepository
import com.example.mitfg.data.medicines.MedicineRepository
import com.example.mitfg.data.users.UserRepository
import com.example.mitfg.domain.model.Alarm
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class responsible for managing the logic and data of the alarm detail screen.
 */
@HiltViewModel
class AlarmDetailViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val medicineRepository: MedicineRepository,
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    // MutableStateFlow to hold the current alarm being managed.
    private val _alarm = MutableStateFlow<Alarm?>(null)
    val alarm = _alarm.asStateFlow()

    // MutableStateFlow to hold the adverse effects of a medicine.
    private val _medicineAdverseEffects = MutableStateFlow<List<String>?>(null)
    val medicineAdverseEffects = _medicineAdverseEffects.asStateFlow()

    // MutableStateFlow to hold the drug interactions of a user.
    private val _userDrugInteractions = MutableStateFlow<List<String>?>(null)
    val userDrugInteractions = _userDrugInteractions.asStateFlow()

    // MutableStateFlow to hold the critical interactions between a user's drug interactions
    // and the adverse effects of a medicine.
    private val _userCriticalInteractions = MutableStateFlow<List<String>?>(null)
    val userCriticalInteractions = _userCriticalInteractions.asStateFlow()

    // MutableStateFlow to indicate whether both lists of adverse effects and drug interactions
    // are filled and not null.
    private val _bothListsAreFilled = MutableStateFlow<Boolean>(false)
    val bothListsAreFilled = _bothListsAreFilled.asStateFlow()

    /**
     * Fetches an alarm by its ID from the repository and updates the _alarm StateFlow.
     * @param id ID of the alarm to fetch.
     */
    fun getAlarmById(id: Long) {
        viewModelScope.launch {
            alarmRepository.getAlarmById(id).collect { retrievedAlarm ->
                _alarm.update { retrievedAlarm }
            }
        }
    }

    /**
     * Fetches medicine by name from the repository and updates the _medicineAdverseEffects StateFlow.
     * Also triggers the retrieval of user drug interactions.
     * @param medicineName Name of the medicine to fetch.
     */
    fun getMedicineByName(medicineName: String) {
        viewModelScope.launch {
            medicineRepository.getMedicineByName(medicineName).fold(
                onSuccess = { foundMedicine ->
                    // Updates the medicine adverse effects list
                    _medicineAdverseEffects.update { foundMedicine!!.adverseEffects }

                    getPatientDrugInteractions()

                    // If both lists are not null, the boolean value updates to true
                    adverseEffectsAndDrugInteractionsAreNotNull()
                },
                onFailure = { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }

    /**
     * Fetches user drug interactions from the repository and updates the _userDrugInteractions StateFlow.
     * Also triggers the retrieval of medicine adverse effects for the user.
     */
    fun getPatientDrugInteractions() {
        viewModelScope.launch {
            val patientEmail = auth.currentUser!!.email.toString()

            userRepository.getDrugInteractionsByEmail(patientEmail).fold(
                onSuccess = { foundList ->
                    _userDrugInteractions.update { foundList }

                    // If both lists are not null, the boolean value updates to true
                    adverseEffectsAndDrugInteractionsAreNotNull()

                    getMedicineAdverseEffectsForUser()
                },
                onFailure = { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }

    /**
     * Finds critical interactions between user drug interactions and medicine adverse effects.
     * Updates the _userCriticalInteractions StateFlow.
     */
    fun getMedicineAdverseEffectsForUser() {
        val userDrugInteractions = ArrayList<String>()

        // If an element is present in both collections, the element is added to the list
        for (element in _userDrugInteractions.value!!) {
            if (element in _medicineAdverseEffects.value!!) {
                userDrugInteractions.add(element)
            }
        }

        _userCriticalInteractions.update { userDrugInteractions }
    }

    /**
     * Checks if both the medicine adverse effects and user drug interactions lists are not null.
     * If both lists are not null, updates the _bothListsAreFilled StateFlow to indicate that both lists are filled.
     */
    fun adverseEffectsAndDrugInteractionsAreNotNull() {
        if (_medicineAdverseEffects.value != null && _userDrugInteractions.value != null) {
            _bothListsAreFilled.update { true }
        }
    }

}