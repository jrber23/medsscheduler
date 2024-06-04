/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

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

/**
 * The AlarmCreationActivity's View Model. It handles
 * every data that's used by the activity
 */
@HiltViewModel
class AlarmCreationViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository,
    private val alarmRepository: AlarmRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    // MutableStateFlow for holding the list of medicines
    private val _medicinesList = MutableStateFlow<List<Medicine?>>(listOf())
    val medicinesList = _medicinesList.asStateFlow()

    // MutableStateFlow for holding the alarm object
    private val _alarm = MutableStateFlow(Alarm(0, "", "","",0,0,0, 0, 0))
    val alarm = _alarm.asStateFlow()

    // MutableStateFlow for holding the value that shows if the alarm is completed
    private val _alarmIsCompleted = MutableStateFlow<Boolean>(false)
    val alarmIsCompleted = _alarmIsCompleted.asStateFlow()

    // MutableStateFlow for holding the current progress var value
    private var _progressBarValue = MutableStateFlow(0)
    val progressBarValue = _progressBarValue.asStateFlow()

    // Object that handles the progress bar increase value
    companion object {
        private const val PROGRESS_BAR_INCREASE = 100/4
    }

    // When the activity is created, the medicines list is retrieved
    init {
        getMedicinesList()
    }

    /**
     * Increase the progress bar value with PROGRESS_BAR_INCREASE
     */
    fun increaseProgressBar() {
        viewModelScope.launch {
            _progressBarValue.update { it + PROGRESS_BAR_INCREASE }
        }
    }

    /**
     * Adds a new alarm to the database.
     */
    fun addAlarm() {
        viewModelScope.launch {
            val id = alarmRepository.addAlarm(_alarm.value)

            _alarm.update { alarm ->
                alarm.copy(id = id)
            }

            _alarmIsCompleted.update { true }
        }
    }

    /**
     * Decrease the progress bar value with PROGRESS_BAR_INCREASE
     */
    fun decreaseProgressBar() {
        viewModelScope.launch {
            _progressBarValue.update { it - PROGRESS_BAR_INCREASE }
        }
    }

    /**
     * Assigns a new frecuency depending on the string value selected
     *
     * @param frequencyString The value selected by the user
     */
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

    /**
     * Assigns a dosage quantity to the alarm
     *
     * @param quantity The quantity to assign
     */
    fun assignDosageQuantity(quantity: String) {
        _alarm.update { alarm ->
            alarm.copy(quantity = quantity)
        }
    }

    /**
     * Assigns a medicine name to the alarm
     *
     * @param medicineName The medicine name to assign
     */
    fun assignMedicineName(medicineName : String) {
        _alarm.update { alarm ->
            alarm.copy(medicineName = medicineName)
        }
    }

    /**
     * Assigns an hour start to the alarm
     *
     * @param newHourStart The hour start to assign
     */
    fun assignHourStart(newHourStart : Int) {
        _alarm.update { alarm ->
            alarm.copy(hourStart = newHourStart)
        }
    }

    /**
     * Assigns a minute start to the alarm
     *
     * @param newMinuteStart The minute start to assign
     */
    fun assignMinuteStart(newMinuteStart : Int) {
        _alarm.update { alarm ->
            alarm.copy(minuteStart = newMinuteStart)
        }
    }

    /**
     * Retrieves all existing medicines from the database.
     */
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

    /**
     * Assigns a medicine presentation to the alarm
     *
     * @param presentation The medicine presentation to assign
     */
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

    /**
     * Retrieves the alarm current medicine presentation
     *
     * @return the current medicine presentation
     */
    fun getMedicinePresentation(): String {
        return _alarm.value.medicinePresentation
    }
}
