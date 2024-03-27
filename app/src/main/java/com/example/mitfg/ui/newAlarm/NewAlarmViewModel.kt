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

@HiltViewModel
class NewAlarmViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    private val _alarmList = MutableStateFlow<List<Alarm>>(emptyList())
    val alarmList : StateFlow<List<Alarm>> = _alarmList.asStateFlow()

    private val _alarmIdToDelete = MutableStateFlow<Long?>(null)
    val alarmIdToDelete : StateFlow<Long?> = _alarmIdToDelete.asStateFlow()

    fun deleteAlarmAtPosition(position: Int)  {
        viewModelScope.launch {
            val alarm = _alarmList.value[position]
            alarmRepository.deleteAlarm(alarm)

            _alarmIdToDelete.update { alarm.id }
        }
    }

    init {
        viewModelScope.launch {
            alarmRepository.getAllAlarms().collect { list ->
                _alarmList.value = list
            }
        }
    }

}