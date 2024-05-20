/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.instructionAlarm

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mitfg.data.instructionAlarm.model.AlarmDto

@Database(entities = [AlarmDto::class], version = 6)
abstract class AlarmDatabase : RoomDatabase() {

    abstract fun alarmDao() : AlarmDao

}