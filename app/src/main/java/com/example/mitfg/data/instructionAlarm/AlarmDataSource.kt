package com.example.mitfg.data.instructionAlarm

import com.example.mitfg.data.instructionAlarm.model.AlarmDto
import kotlinx.coroutines.flow.Flow

interface AlarmDataSource {

    suspend fun addAlarm(alarm: AlarmDto) : Long

    suspend fun deleteAlarm(alarm: AlarmDto)

    fun getAllAlarms() : Flow<List<AlarmDto>>

    suspend fun deleteAllAlarms()

    fun getAlarmById(id: Long): Flow<AlarmDto?>

    suspend fun addDosageToAlarm(id: Long) : Int

    suspend fun addTakenDosageToAlarm(id: Long) : Int

}