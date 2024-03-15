package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitfg.data.medicines.MedicineRepository
import com.example.mitfg.domain.model.Medicine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmCreationViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository
): ViewModel() {
    private val _medicinesList = MutableStateFlow<List<Medicine?>>(listOf())
    val medicinesList = _medicinesList.asStateFlow()

    private var _progressBarValue = MutableLiveData<Int>(0)
    val progressBarValue: LiveData<Int> get() = _progressBarValue

    init {
        getMedicinesList()
    }

    private fun getMedicinesList() {
        viewModelScope.launch {
            medicineRepository.getAllMedicines().fold(
                onSuccess = { list ->
                    _medicinesList.update { list }
                },
                onFailure = { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }

    fun increseProgressBar() {
        _progressBarValue.value = _progressBarValue.value!! + 20
    }

    fun decreseProgressBar() {
        _progressBarValue.value = _progressBarValue.value!! - 20
    }
}