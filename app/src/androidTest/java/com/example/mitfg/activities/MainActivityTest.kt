package com.example.mitfg.activities

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mitfg.R
import com.example.mitfg.ui.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = activityScenarioRule<MainActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun loginOfDoctor_ShouldDisplayDoctorFunctions() {
        activityRule.scenario.onActivity() { activity ->
            activity.showDoctorNavBar()
        }

        activityRule.scenario.use {
            Espresso.onView(ViewMatchers.withId(R.id.newAlarmFragment))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }



}