package com.example.mitfg.data.instructionAlarm

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mitfg.data.instructionAlarm.model.AlarmDto

@Database(entities = [AlarmDto::class], version = 5)
abstract class AlarmDatabase : RoomDatabase() {

    abstract fun alarmDao() : AlarmDao

}