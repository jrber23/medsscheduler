/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.instructionAlarm

import com.example.mitfg.domain.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    suspend fun addAlarm(alarm: Alarm) : Long

    suspend fun deleteAlarm(alarm: Alarm)

    fun getAllAlarms() : Flow<List<Alarm>>

    suspend fun deleteAllAlarms()

    fun getAlarmById(id: Long): Flow<Alarm>

    suspend fun addDosageToAlarm(id: Long) : Int

    suspend fun addTakenDosageToAlarm(id: Long) : Int
}