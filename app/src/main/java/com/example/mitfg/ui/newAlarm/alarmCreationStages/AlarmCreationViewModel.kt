package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.app.AlarmManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitfg.R
import com.example.mitfg.data.instructionAlarm.AlarmRepository
import com.example.mitfg.data.medicines.MedicineRepository
import com.example.mitfg.domain.model.Alarm
import com.example.mitfg.domain.model.Medicine
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmCreationViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository,
    private val alarmRepository: AlarmRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _medicinesList = MutableStateFlow<List<Medicine?>>(listOf())
    val medicinesList = _medicinesList.asStateFlow()

    private val _alarm = MutableStateFlow(Alarm(0, "", "","",0,0,0, 0, 0))
    val alarm = _alarm.asStateFlow()

    private val _alarmIsCompleted = MutableStateFlow<Boolean>(false)
    val alarmIsCompleted = _alarmIsCompleted.asStateFlow()

    private var _progressBarValue = MutableStateFlow(0)
    val progressBarValue = _progressBarValue.asStateFlow()

    companion object {
        private const val PROGRESS_BAR_INCREASE = 100/4
    }

    init {
        getMedicinesList()
    }

    fun increaseProgressBar() {
        viewModelScope.launch {
            _progressBarValue.update { it + PROGRESS_BAR_INCREASE }
        }
    }

    fun addAlarm() {
        viewModelScope.launch {
            val id = alarmRepository.addAlarm(_alarm.value)

            _alarm.update { alarm ->
                alarm.copy(id = id)
            }

            _alarmIsCompleted.update { true }
        }
    }

    fun decreaseProgressBar() {
        viewModelScope.launch {
            _progressBarValue.update { it - PROGRESS_BAR_INCREASE }
        }
    }

    fun assignFrequencyInstruction(frequencyString: String) {
        var frequency : Long = 0
        when (frequencyString) {
            context.getString(R.string.hours24) -> frequency = AlarmManager.INTERVAL_DAY
            context.getString(R.string.hours12) -> frequency = AlarmManager.INTERVAL_HALF_DAY
            context.getString(R.string.hours1) -> frequency = AlarmManager.INTERVAL_HOUR
            context.getString(R.string.minutes30) -> frequency = AlarmManager.INTERVAL_HALF_HOUR
            context.getString(R.string.minutes15) -> frequency = AlarmManager.INTERVAL_FIFTEEN_MINUTES
        }

        _alarm.update { alarm ->
            alarm.copy(frequency = frequency)
        }
    }

    fun assignPillQuantity(quantity: String) {
        _alarm.update { alarm ->
            alarm.copy(quantity = quantity)
        }

        Log.d("DOSAGE", _alarm.value.quantity.toString())
    }

    fun assignMedicineName(medicineName : String) {
        _alarm.update { alarm ->
            alarm.copy(medicineName = medicineName)
        }
    }

    fun assignHourStart(newHourStart : Int) {
        _alarm.update { alarm ->
            alarm.copy(hourStart = newHourStart)
        }
    }

    fun assignMinuteStart(newMinuteStart : Int) {
        _alarm.update { alarm ->
            alarm.copy(minuteStart = newMinuteStart)
        }
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

    fun assignMedicinePresentation(presentation: String) {
        _alarm.update { alarm ->
            var presentationAbrev = ""

            when (presentation) {
                context.getString(R.string.tvPill) -> presentationAbrev = context.getString(R.string.pillAbrev)
                context.getString(R.string.tvPacket) -> presentationAbrev = context.getString(R.string.packetAbrev)
                context.getString(R.string.tvMililitres) -> presentationAbrev = context.getString(R.string.MlAbrev)
            }

            alarm.copy(medicinePresentation = presentationAbrev)
        }
    }

    fun getMedicinePresentation(): String {
        return _alarm.value.medicinePresentation
    }

    fun sumale() {
        viewModelScope.launch {
            val rows = alarmRepository.addDosageToAlarm(_alarm.value.id)
            Log.d("SE SUMA UNA DOSIS", rows.toString())
        }
    }
}
