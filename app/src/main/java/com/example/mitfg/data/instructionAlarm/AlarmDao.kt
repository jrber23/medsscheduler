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
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_ID
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_TAKEN_DOSAGES
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_TOTAL_DOSAGES
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.TABLE_NAME
import com.example.mitfg.data.instructionAlarm.model.AlarmDto
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlarm(alarm: AlarmDto) : Long

    @Delete
    suspend fun deleteAlarm(vararg alarm: AlarmDto)

    @Query("SELECT * FROM ${TABLE_NAME}")
    fun getAllAlarms() : Flow<List<AlarmDto>>

    @Query("DELETE FROM ${TABLE_NAME}")
    suspend fun deleteAllAlarms()

    @Query("SELECT * FROM ${TABLE_NAME} WHERE ${COLUMN_ID} = :id")
    fun getAlarmById(id: Long): Flow<AlarmDto?>

    @Query("UPDATE OR IGNORE ${TABLE_NAME} SET ${COLUMN_TOTAL_DOSAGES} = ${COLUMN_TOTAL_DOSAGES} + 1 WHERE ${COLUMN_ID} = :id")
    suspend fun addDosageToAlarm(id: Long) : Int

    @Query("UPDATE OR IGNORE ${TABLE_NAME} SET ${COLUMN_TAKEN_DOSAGES} = ${COLUMN_TAKEN_DOSAGES} + 1 WHERE ${COLUMN_ID} = :id")
    suspend fun addTakenDosageToAlarm(id: Long) : Int


}