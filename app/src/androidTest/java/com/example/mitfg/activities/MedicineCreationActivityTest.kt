/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mitfg.R
import com.example.mitfg.ui.doctor.DoctorActivity
import com.example.mitfg.ui.medicineCreation.MedicineCreationActivity
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
class MedicineCreationActivityTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = activityScenarioRule<MedicineCreationActivity>()

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    @UiThread
    fun editTextMedicineNameEmpty_shouldShowDialog() {
        activityRule.scenario.use {
            onView(withId(R.id.etMedicineName)).perform(clearText())
            onView(withId(R.id.etMedicineDescription)).perform(clearText())

            onView(withId(R.id.etMedicineDescription)).perform(replaceText("abcd"))

            onView(withId(R.id.bCrearMedicamento)).perform(click())

            Thread.sleep(3000)

            onView(withText("Se debe asignar un nombre y una descripción al nuevo medicamento")).check(matches(isDisplayed()))
        }
    }

    @Test
    @UiThread
    fun editTextMedicineDescriptionEmpty_shouldShowDialog() {
        activityRule.scenario.use {
            onView(withId(R.id.etMedicineName)).perform(clearText())
            onView(withId(R.id.etMedicineDescription)).perform(clearText())

            onView(withId(R.id.etMedicineName)).perform(replaceText("abcd"))

            onView(withId(R.id.bCrearMedicamento)).perform(click())

            Thread.sleep(3000)

            onView(withText("Se debe asignar un nombre y una descripción al nuevo medicamento")).check(matches(isDisplayed()))
        }
    }

    @Test
    @UiThread
    fun noEditTextEmpty_shouldNotShowDialog() {
        activityRule.scenario.use {
            onView(withId(R.id.etMedicineName)).perform(clearText())
            onView(withId(R.id.etMedicineDescription)).perform(clearText())

            onView(withId(R.id.etMedicineName)).perform(replaceText("abcd"))
            onView(withId(R.id.etMedicineDescription)).perform(replaceText("abcd"))

            onView(withId(R.id.bCrearMedicamento)).perform(click())

            Thread.sleep(3000)

            onView(withText("Se debe asignar un nombre y una descripción al nuevo medicamento")).check(
                doesNotExist()
            )
        }
    }

    @Test
    @UiThread
    fun noEditTextEmpty_shouldSwapToDoctorActivity() {
        activityRule.scenario.use {
            onView(withId(R.id.etMedicineName)).perform(clearText())
            onView(withId(R.id.etMedicineDescription)).perform(clearText())

            onView(withId(R.id.etMedicineName)).perform(replaceText("abcd"))
            onView(withId(R.id.etMedicineDescription)).perform(replaceText("abcd"))

            onView(withId(R.id.bCrearMedicamento)).perform(click())

            Thread.sleep(3000)

            Intents.intended(hasComponent(DoctorActivity::class.java.name))
        }
    }

}