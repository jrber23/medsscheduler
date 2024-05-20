/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.instructionAlarm

import com.example.mitfg.data.instructionAlarm.model.toDomain
import com.example.mitfg.data.instructionAlarm.model.toDto
import com.example.mitfg.domain.model.Alarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmDataSource: AlarmDataSource
) : AlarmRepository {
    override suspend fun addAlarm(alarm: Alarm) : Long {
        return alarmDataSource.addAlarm(alarm.toDto())
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmDataSource.deleteAlarm(alarm.toDto())
    }

    override fun getAllAlarms(): Flow<List<Alarm>> =
        alarmDataSource.getAllAlarms().map { list ->
            list.map { alarmDto ->
                alarmDto.toDomain()
            }
        }


    override suspend fun deleteAllAlarms() {
        alarmDataSource.deleteAllAlarms()
    }

    override fun getAlarmById(id: Long): Flow<Alarm> =
        alarmDataSource.getAlarmById(id).map { alarmDto ->
            alarmDto!!.toDomain()
        }

    override suspend fun addDosageToAlarm(id: Long) : Int{
        return alarmDataSource.addDosageToAlarm(id)
    }

    override suspend fun addTakenDosageToAlarm(id: Long) : Int {
        return alarmDataSource.addTakenDosageToAlarm(id)
    }
}