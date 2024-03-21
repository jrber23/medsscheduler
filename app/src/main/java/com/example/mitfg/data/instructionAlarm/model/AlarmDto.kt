package com.example.mitfg.data.instructionAlarm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_FREQUENCY
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_HOUR_START
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_ID
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_MEDICINE_NAME
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_MINUTE_START
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.COLUMN_QUANTITY
import com.example.mitfg.data.instructionAlarm.AlarmContact.AlarmTable.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class AlarmDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = COLUMN_ID) val id: Long,
    @ColumnInfo(name = COLUMN_MEDICINE_NAME) val medicineName: String,
    @ColumnInfo(name = COLUMN_QUANTITY) val quantity: Int,
    @ColumnInfo(name = COLUMN_FREQUENCY) val frequency: Long,
    @ColumnInfo(name = COLUMN_HOUR_START) val hourStart: Int,
    @ColumnInfo(name = COLUMN_MINUTE_START) val minuteStart: Int
)
