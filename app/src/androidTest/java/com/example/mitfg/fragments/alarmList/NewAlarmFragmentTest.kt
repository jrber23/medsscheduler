/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.fragments.alarmList

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mitfg.R
import com.example.mitfg.ui.main.MainActivity
import com.example.mitfg.ui.newAlarm.alarmCreationStages.AlarmCreationActivity
import com.google.firebase.annotations.concurrent.UiThread
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NewAlarmFragmentTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @UiThread
    @Test
    fun clickOnCreateAlarm_goesToAlarmCreationActivity() {
        val scenario = activityScenarioRule.scenario
        scenario.use {
            pressBack()
            onView(withId(R.id.createNewAlarm)).perform(click())

            Thread.sleep(3000)

            Intents.intended(IntentMatchers.hasComponent(AlarmCreationActivity::class.java.name))
        }
    }


}