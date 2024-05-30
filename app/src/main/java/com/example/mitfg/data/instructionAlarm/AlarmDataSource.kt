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

interface AlarmDataSource {

    /**
     * Add a new alarm to the database
     * @param alarm the DTO that contains all the required data
     */
    suspend fun addAlarm(alarm: AlarmDto) : Long

    /**
     * Deletes the given alarm from the database
     * @param alarm the alarm to delete
     */
    suspend fun deleteAlarm(alarm: AlarmDto)

    /**
     * Retrieves every existing alarm of the database
     * @return An encapsulation of a list that contains all the alarms in the database
     */
    fun getAllAlarms() : Flow<List<AlarmDto>>

    /**
     * Deletes all the alarms in the database
     */
    suspend fun deleteAllAlarms()

    /**
     * Retrieves the alarm with the given ID from the database
     * @param id the ID of the alarm to be retrieved
     * @return An encapsulation of the retrieved alarm
     */
    fun getAlarmById(id: Long): Flow<AlarmDto?>

    /**
     * Adds a dosage to the total dosages of the alarm with the given ID
     * @param id the ID of the alarm to increase its total dosages
     * @return The new number of dosages
     */
    suspend fun addDosageToAlarm(id: Long) : Int

    /**
     * Add a taken dosage to the number of total taken dosages of the alarm with the given ID
     * @param id the ID of the alarm to add taken dosages
     * @return The new number of taken dosages
     */
    suspend fun addTakenDosageToAlarm(id: Long) : Int

}