package com.example.mitfg.viewModelTesting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mitfg.data.FakeAlarmRepository
import com.example.mitfg.ui.newAlarm.NewAlarmViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewAlarmViewModelTest {

    private lateinit var alarmRepository: FakeAlarmRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        alarmRepository = FakeAlarmRepository()
    }

    @Test
    fun getAllAlarms_providesAlarmList() = runTest {
        // Given
        val newAlarmViewModel = NewAlarmViewModel(
            alarmRepository = alarmRepository
        )

        // When
        backgroundScope.launch {
            // Then
            newAlarmViewModel.alarmList.collect { listOfAlarms ->
                assertTrue(listOfAlarms.isNotEmpty())
                assertEquals(3, listOfAlarms.size)
            }
        }
    }

    @Test
    fun deleteAlarmAtPosition_deletesFromRoomDatabase() = runTest {
        // Given
        val newAlarmViewModel = NewAlarmViewModel(
            alarmRepository = alarmRepository
        )

        backgroundScope.launch {
            // When
            newAlarmViewModel.alarmList.collect {
                // Then
                newAlarmViewModel.deleteAlarmAtPosition(0)
                assertEquals(2, newAlarmViewModel.alarmList.value.size)
            }
        }



    }

}