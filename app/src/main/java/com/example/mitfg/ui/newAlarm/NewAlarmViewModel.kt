/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.newAlarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitfg.data.instructionAlarm.AlarmRepository
import com.example.mitfg.domain.model.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing alarm data.
 * Handles operations like fetching alarm list and deleting alarms.
 */
@HiltViewModel
class NewAlarmViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    // MutableStateFlow for holding the list of alarms
    private val _alarmList = MutableStateFlow<List<Alarm>>(emptyList())
    val alarmList : StateFlow<List<Alarm>> = _alarmList.asStateFlow()

    // MutableStateFlow for holding the ID of the alarm to be deleted
    private val _alarmIdToDelete = MutableStateFlow<Long?>(null)
    val alarmIdToDelete : StateFlow<Long?> = _alarmIdToDelete.asStateFlow()

    /**
     * Deletes the alarm at the specified position in the list.
     * Updates the ID of the deleted alarm.
     *
     * @param position The position of the alarm to be deleted in the list.
     */
    fun deleteAlarmAtPosition(position: Int)  {
        viewModelScope.launch {
            val alarm = _alarmList.value[position]
            alarmRepository.deleteAlarm(alarm)

            // Updates the ID of the alarm to be deleted
            _alarmIdToDelete.update { alarm.id }
        }
    }

    /**
     * Initializes the ViewModel.
     * Fetches the list of all alarms from the repository and updates the _alarmList.
     */
    init {
        viewModelScope.launch {
            alarmRepository.getAllAlarms().collect { list ->
                _alarmList.update { list }
            }
        }
    }

}