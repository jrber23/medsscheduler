/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.room

import android.app.AlarmManager
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mitfg.data.instructionAlarm.AlarmDao
import com.example.mitfg.data.instructionAlarm.AlarmDatabase
import com.example.mitfg.data.instructionAlarm.model.toDto
import com.example.mitfg.domain.model.Alarm
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class AlarmDatabaseTest {

    @JvmField
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var alarmDao: AlarmDao
    private lateinit var alarmDatabase: AlarmDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        alarmDatabase = Room.inMemoryDatabaseBuilder(
            context,
            AlarmDatabase::class.java
        ).build()
        alarmDao = alarmDatabase.alarmDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        alarmDatabase.close()
    }

    @Test
    fun getAllAlarms_Test() = runTest {
        // Given
        val objectsToAdd = listOf(
            Alarm(0, "Biotina", "P", "4", AlarmManager.INTERVAL_HOUR, 15, 0, 0, 0),
            Alarm(1, "Paracetamol", "Ml", "3", AlarmManager.INTERVAL_HALF_HOUR, 16, 0, 0, 0),
            Alarm(2, "Aspirina", "Pack", "2", AlarmManager.INTERVAL_FIFTEEN_MINUTES, 17, 30, 0, 0)
        )

        // When
        objectsToAdd.forEach { alarmDao.addAlarm(it.toDto()) }

        // Then
        backgroundScope.launch {
            alarmDao.getAllAlarms().collect { list ->
                assertTrue(list.isNotEmpty())
            }
        }
    }

    @Test
    fun addAlarm_Test() = runTest {
        // Given
        val alarm = Alarm(3, "Frenadol", "P", "1", AlarmManager.INTERVAL_HALF_DAY, 20, 0, 0, 0)

        // When
        backgroundScope.launch {
            alarmDao.addAlarm(alarm.toDto())

            // Then
            alarmDao.getAllAlarms().collect { list ->
                assertEquals(1, list.size)
            }
        }
    }

    @Test
    fun deleteAlarm_Test() = runTest {
        // Given
        val alarm = Alarm(3, "Frenadol", "P", "1", AlarmManager.INTERVAL_HALF_DAY, 20, 0, 0, 0)


        backgroundScope.launch {
            alarmDao.addAlarm(alarm.toDto())

            // When
            alarmDao.deleteAlarm(alarm.toDto())

            // Then
            alarmDao.getAllAlarms().collect { list ->
                assertTrue(list.isEmpty())
            }
        }
    }

    @Test
    fun deleteAllAlarms_Test() = runTest {
        // Given
        val alarmsBunch = listOf<Alarm>(
            Alarm(3, "Frenadol", "P", "1", AlarmManager.INTERVAL_HALF_DAY, 20, 10, 0, 0),
            Alarm(4, "Aspirina", "P", "1", AlarmManager.INTERVAL_FIFTEEN_MINUTES, 21, 20, 0, 0),
            Alarm(5, "Ketoconazol", "P", "1", AlarmManager.INTERVAL_HALF_HOUR, 22, 30, 0, 0),
            Alarm(6, "Biotina", "P", "1", AlarmManager.INTERVAL_DAY, 23, 40, 0, 0)
        )


        backgroundScope.launch {
            for (alarm in alarmsBunch) {
                alarmDao.addAlarm(alarm.toDto())
            }

            // When
            alarmDao.deleteAllAlarms()

            // Then
            alarmDao.getAllAlarms().collect { list ->
                assertTrue(list.isEmpty())
            }
        }
    }

}