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
import com.example.mitfg.domain.model.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmDetailViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    private val _alarm = MutableStateFlow<Alarm?>(null)
    val alarm = _alarm.asStateFlow()

    private val _medicineAdverseEffects = MutableStateFlow<List<String>?>(null)
    val medicineAdverseEffects = _medicineAdverseEffects.asStateFlow()

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
                    _medicineAdverseEffects.update { foundMedicine?.adverseEffects }
                },
                onFailure = { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }


}