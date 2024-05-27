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

@HiltViewModel
class AlarmDetailViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val medicineRepository: MedicineRepository,
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _alarm = MutableStateFlow<Alarm?>(null)
    val alarm = _alarm.asStateFlow()

    private val _medicineAdverseEffects = MutableStateFlow<List<String>?>(null)
    val medicineAdverseEffects = _medicineAdverseEffects.asStateFlow()

    private val _userDrugInteractions = MutableStateFlow<List<String>?>(null)
    val userDrugInteractions = _userDrugInteractions.asStateFlow()

    private val _userCriticalInteractions = MutableStateFlow<List<String>?>(null)
    val userCriticalInteractions = _userCriticalInteractions.asStateFlow()

    private val _bothListsAreFilled = MutableStateFlow<Boolean>(false)
    val bothListsAreFilled = _bothListsAreFilled.asStateFlow()

    fun getAlarmById(id: Long) {
        viewModelScope.launch {
            alarmRepository.getAlarmById(id).collect { retrievedAlarm ->
                _alarm.update { retrievedAlarm }
            }
        }
    }

    fun getMedicineByName(medicineName: String) {
        viewModelScope.launch {
            medicineRepository.getMedicineByName(medicineName).fold(
                onSuccess = { foundMedicine ->
                    _medicineAdverseEffects.update { foundMedicine!!.adverseEffects }

                    getPatientDrugInteractions()

                    if (_medicineAdverseEffects.value != null && _userDrugInteractions.value != null) {
                        _bothListsAreFilled.update { true }

                        Log.d("IS_TRUE", _bothListsAreFilled.value.toString())
                    }
                },
                onFailure = { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }

    fun getPatientDrugInteractions() {
        viewModelScope.launch {
            val patientEmail = auth.currentUser!!.email.toString()

            userRepository.getDrugInteractionsByEmail(patientEmail).fold(
                onSuccess = { foundList ->
                    _userDrugInteractions.update { foundList }

                    if (_medicineAdverseEffects.value != null && _userDrugInteractions.value != null) {
                        _bothListsAreFilled.update { true }
                    }

                    getMedicineAdverseEffectsForUser()
                },
                onFailure = { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }

    fun getMedicineAdverseEffectsForUser() {
        val list = ArrayList<String>()

        for (element in _userDrugInteractions.value!!) {
            if (element in _medicineAdverseEffects.value!!) {
                list.add(element)
            }
        }

        _userCriticalInteractions.update { list }

        Log.d("CRITICAL_INTERACTIONS", _userCriticalInteractions.value!!.size.toString())
    }

}