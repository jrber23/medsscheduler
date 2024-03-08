package com.example.mitfg

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class DialogHelperTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun testShowPopUp() {
        activityRule.scenario.onActivity() { activity ->
            activity.showPopUp("Mensaje de prueba")
        }

        onView(withText("Mensaje de prueba")).check(matches(isDisplayed()))
        onView(withText(R.string.accept)).perform(click())

        onView(withText("Mensaje de prueba")).check(doesNotExist())
    }
}