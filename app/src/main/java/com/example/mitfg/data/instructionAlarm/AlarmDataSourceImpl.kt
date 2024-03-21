package com.example.mitfg.data.instructionAlarm

import com.example.mitfg.data.instructionAlarm.model.AlarmDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlarmDataSourceImpl @Inject constructor(
    private val alarmDao: AlarmDao
) : AlarmDataSource {
    override suspend fun addAlarm(alarm: AlarmDto) : Long {
        return alarmDao.addAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: AlarmDto) {
        alarmDao.deleteAlarm(alarm)
    }

    override fun getAllAlarms(): Flow<List<AlarmDto>> =
        alarmDao.getAllAlarms()

    override suspend fun deleteAllAlarms() {
        alarmDao.deleteAllAlarms()
    }
}