package com.example.mitfg.mappers

import android.app.AlarmManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mitfg.data.instructionAlarm.model.AlarmDto
import com.example.mitfg.data.instructionAlarm.model.toDomain
import com.example.mitfg.data.instructionAlarm.model.toDto
import com.example.mitfg.domain.model.Alarm
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AlarmMapperTest {

    @Test
    fun alarmDtoToDomain_Test() {
        val alarmDto = AlarmDto(0, "Biotina", "P", "4", AlarmManager.INTERVAL_HOUR, 15, 0)

        assertTrue(alarmDto.toDomain() is Alarm)
    }

    @Test
    fun alarmToDto_Test() {
        val alarm = Alarm(0, "Biotina", "Ml", "4", AlarmManager.INTERVAL_HOUR, 15, 0)

        assertTrue(alarm.toDto() is AlarmDto)
    }

}