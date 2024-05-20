/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.instructionAlarm

import com.example.mitfg.data.instructionAlarm.model.AlarmDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlarmDataSourceImpl @Inject constructor(
    private val alarmDao: AlarmDao
) : AlarmDataSource {
    override suspend fun addAlarm(alarm: AlarmDto) : Long {
        return alarmDao.addAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: AlarmDto) {
        alarmDao.deleteAlarm(alarm)
    }

    override fun getAllAlarms(): Flow<List<AlarmDto>> =
        alarmDao.getAllAlarms()

    override suspend fun deleteAllAlarms() {
        alarmDao.deleteAllAlarms()
    }

    override fun getAlarmById(id: Long): Flow<AlarmDto?> =
        alarmDao.getAlarmById(id)

    override suspend fun addDosageToAlarm(id: Long) : Int {
        return alarmDao.addDosageToAlarm(id)
    }

    override suspend fun addTakenDosageToAlarm(id: Long) : Int {
        return alarmDao.addTakenDosageToAlarm(id)
    }
}