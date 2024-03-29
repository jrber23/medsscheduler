package com.example.mitfg.data

import android.app.AlarmManager
import com.example.mitfg.data.instructionAlarm.AlarmRepository
import com.example.mitfg.domain.model.Alarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeAlarmRepository @Inject constructor() : AlarmRepository {

    private val fakeAlarms = mutableListOf<Alarm>(
        Alarm(0, "Biotina", 4, AlarmManager.INTERVAL_HOUR, 15, 0),
        Alarm(1, "Paracetamol", 3, AlarmManager.INTERVAL_HALF_HOUR, 16, 0),
        Alarm(2, "Aspirina", 2, AlarmManager.INTERVAL_FIFTEEN_MINUTES, 17, 30),
    )
    override suspend fun addAlarm(alarm: Alarm): Long {
        fakeAlarms.add(alarm)
        return alarm.id
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        fakeAlarms.remove(alarm)
    }

    override fun getAllAlarms(): Flow<List<Alarm>> {
        return flow { emit(fakeAlarms) }
    }

    override suspend fun deleteAllAlarms() {
        fakeAlarms.clear()
    }
}