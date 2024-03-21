package com.example.mitfg.data.instructionAlarm

import com.example.mitfg.domain.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    suspend fun addAlarm(alarm: Alarm) : Long

    suspend fun deleteAlarm(alarm: Alarm)

    fun getAllAlarms() : Flow<List<Alarm>>

    suspend fun deleteAllAlarms()

}