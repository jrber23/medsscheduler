/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.instructionAlarm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mitfg.data.instructionAlarm.AlarmContract.AlarmTable.COLUMN_ID
import com.example.mitfg.data.instructionAlarm.AlarmContract.AlarmTable.COLUMN_TAKEN_DOSAGES
import com.example.mitfg.data.instructionAlarm.AlarmContract.AlarmTable.COLUMN_TOTAL_DOSAGES
import com.example.mitfg.data.instructionAlarm.AlarmContract.AlarmTable.TABLE_NAME
import com.example.mitfg.data.instructionAlarm.model.AlarmDto
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    /**
     * Add a new alarm to the database
     * @param alarm the DTO that contains all the required data
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlarm(alarm: AlarmDto) : Long

    /**
     * Deletes the given alarm from the database
     * @param alarm the alarm to delete
     */
    @Delete
    suspend fun deleteAlarm(vararg alarm: AlarmDto)

    /**
     * Retrieves every existing alarm of the database
     * @return An encapsulation of a list that contains all the alarms in the database
     */
    @Query("SELECT * FROM ${TABLE_NAME}")
    fun getAllAlarms() : Flow<List<AlarmDto>>

    /**
     * Deletes all the alarms in the database
     */
    @Query("DELETE FROM ${TABLE_NAME}")
    suspend fun deleteAllAlarms()

    /**
     * Retrieves the alarm with the given ID from the database
     * @param id the ID of the alarm to be retrieved
     * @return An encapsulation of the retrieved alarm
     */
    @Query("SELECT * FROM ${TABLE_NAME} WHERE ${COLUMN_ID} = :id")
    fun getAlarmById(id: Long): Flow<AlarmDto?>

    /**
     * Adds a dosage to the total dosages of the alarm with the given ID
     * @param id the ID of the alarm to increase its total dosages
     * @return The new number of dosages
     */
    @Query("UPDATE OR IGNORE ${TABLE_NAME} SET ${COLUMN_TOTAL_DOSAGES} = ${COLUMN_TOTAL_DOSAGES} + 1 WHERE ${COLUMN_ID} = :id")
    suspend fun addDosageToAlarm(id: Long) : Int

    /**
     * Add a taken dosage to the number of total taken dosages of the alarm with the given ID
     * @param id the ID of the alarm to add taken dosages
     * @return The new number of taken dosages
     */
    @Query("UPDATE OR IGNORE ${TABLE_NAME} SET ${COLUMN_TAKEN_DOSAGES} = ${COLUMN_TAKEN_DOSAGES} + 1 WHERE ${COLUMN_ID} = :id")
    suspend fun addTakenDosageToAlarm(id: Long) : Int


}