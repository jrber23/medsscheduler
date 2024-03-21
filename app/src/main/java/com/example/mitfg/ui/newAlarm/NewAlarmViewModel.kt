package com.example.mitfg.ui.newAlarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitfg.data.instructionAlarm.AlarmRepository
import com.example.mitfg.domain.model.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewAlarmViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    private val _alarmList = MutableStateFlow<List<Alarm>>(emptyList())
    val alarmList : StateFlow<List<Alarm>> = _alarmList.asStateFlow()

    init {
        viewModelScope.launch {
            alarmRepository.getAllAlarms().collect { list ->
                _alarmList.value = list
            }
        }
    }

}