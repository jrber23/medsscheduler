/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.instructionAlarm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_FREQUENCY
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_HOUR_START
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_ID
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_MEDICINE_NAME
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_MEDICINE_PRESENTATION
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_MINUTE_START
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_QUANTITY
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_TAKEN_DOSAGES
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_TOTAL_DOSAGES
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class AlarmDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = COLUMN_ID) val id: Long,
    @ColumnInfo(name = COLUMN_MEDICINE_NAME) val medicineName: String,
    @ColumnInfo(name = COLUMN_MEDICINE_PRESENTATION) val medicinePresentation: String,
    @ColumnInfo(name = COLUMN_QUANTITY) val quantity: String,
    @ColumnInfo(name = COLUMN_FREQUENCY) val frequency: Long,
    @ColumnInfo(name = COLUMN_HOUR_START) val hourStart: Int,
    @ColumnInfo(name = COLUMN_MINUTE_START) val minuteStart: Int,
    @ColumnInfo(name = COLUMN_TOTAL_DOSAGES) val totalDosages: Int,
    @ColumnInfo(name = COLUMN_TAKEN_DOSAGES) val takenDosages: Int
)
