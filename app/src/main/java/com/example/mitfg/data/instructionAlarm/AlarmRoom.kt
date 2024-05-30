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

class AlarmRoom @Inject constructor(
    private val alarmDao: AlarmDao
) : AlarmDataSource {

    /**
     * Add a new alarm to the database
     * @param alarm the DTO that contains all the required data
     */
    override suspend fun addAlarm(alarm: AlarmDto) : Long {
        return alarmDao.addAlarm(alarm)
    }

    /**
     * Deletes the given alarm from the database
     * @param alarm the alarm to delete
     */
    override suspend fun deleteAlarm(alarm: AlarmDto) {
        alarmDao.deleteAlarm(alarm)
    }

    /**
     * Retrieves every existing alarm of the database
     * @return An encapsulation of a list that contains all the alarms in the database
     */
    override fun getAllAlarms(): Flow<List<AlarmDto>> =
        alarmDao.getAllAlarms()

    /**
     * Deletes all the alarms in the database
     */
    override suspend fun deleteAllAlarms() {
        alarmDao.deleteAllAlarms()
    }

    /**
     * Retrieves the alarm with the given ID from the database
     * @param id the ID of the alarm to be retrieved
     * @return An encapsulation of the retrieved alarm
     */
    override fun getAlarmById(id: Long): Flow<AlarmDto?> =
        alarmDao.getAlarmById(id)

    /**
     * Adds a dosage to the total dosages of the alarm with the given ID
     * @param id the ID of the alarm to increase its total dosages
     * @return The new number of dosages
     */
    override suspend fun addDosageToAlarm(id: Long) : Int {
        return alarmDao.addDosageToAlarm(id)
    }

    /**
     * Add a taken dosage to the number of total taken dosages of the alarm with the given ID
     * @param id the ID of the alarm to add taken dosages
     * @return The new number of taken dosages
     */
    override suspend fun addTakenDosageToAlarm(id: Long) : Int {
        return alarmDao.addTakenDosageToAlarm(id)
    }
}