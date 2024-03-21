package com.example.mitfg.data.instructionAlarm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

}