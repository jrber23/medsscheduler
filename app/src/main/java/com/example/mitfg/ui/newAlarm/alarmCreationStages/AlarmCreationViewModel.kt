package com.example.mitfg.ui.newAlarm.alarmCreationStages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlarmCreationViewModel: ViewModel() {
    private var _progressBarValue = MutableLiveData<Int>(0)
    val progressBarValue: LiveData<Int> get() = _progressBarValue

    fun increseProgressBar() {
        _progressBarValue.value = _progressBarValue.value!! + 50
    }

    fun decreseProgressBar() {
        _progressBarValue.value = _progressBarValue.value!! - 50
    }
}