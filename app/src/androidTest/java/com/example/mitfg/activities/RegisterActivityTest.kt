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
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mitfg.R
import com.example.mitfg.ui.main.MainActivity
import com.example.mitfg.ui.register.RegisterActivity
import com.google.firebase.annotations.concurrent.UiThread
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class RegisterActivityTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = activityScenarioRule<RegisterActivity>()

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun showPopUp_Test() {
        val countDownLatch = CountDownLatch(1)

        activityRule.scenario.onActivity() { activity ->
            activity.showPopUp("Se deben cumplimentar todos los campos")

            countDownLatch.countDown()
        }

        countDownLatch.await()

        activityRule.scenario.use {
            onView(ViewMatchers.withText("Se deben cumplimentar todos los campos"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            onView(ViewMatchers.withText(R.string.accept)).perform(ViewActions.click())

            onView(ViewMatchers.withText("Se deben cumplimentar todos los campos"))
                .check(ViewAssertions.doesNotExist())
        }
    }

    @Test
    @UiThread
    fun bothEmailAndPasswordEmpty_ShouldNotBeAllowed() {
        activityRule.scenario.use {
            // PopUp con campos vacíos
            onView(ViewMatchers.withId(R.id.bRegister)).perform(ViewActions.click())

            onView(ViewMatchers.withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(
                ViewAssertions.matches(ViewMatchers.isDisplayed())
            )
            onView(ViewMatchers.withText(R.string.accept)).perform(ViewActions.click())

            onView(ViewMatchers.withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(
                ViewAssertions.doesNotExist()
            )
        }
    }

    @Test
    @UiThread
    fun passwordEmpty_ShouldNotBeAllowed() {
        activityRule.scenario.use {
            // PopUp con solo password vacío
            onView(ViewMatchers.withId(R.id.tiet_email)).perform(ViewActions.clearText())
            onView(ViewMatchers.withId(R.id.tiet_email)).perform(ViewActions.replaceText("pepepp@outlook.com"))

            onView(ViewMatchers.withId(R.id.bRegister)).perform(ViewActions.click())

            onView(ViewMatchers.withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(
                ViewAssertions.matches(ViewMatchers.isDisplayed())
            )
            onView(ViewMatchers.withText(R.string.accept)).perform(ViewActions.click())

            onView(ViewMatchers.withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(
                ViewAssertions.doesNotExist()
            )
        }
    }

    @Test
    @UiThread
    fun emailEmpty_ShouldNotBeAllowed() {
        activityRule.scenario.use {
            // PopUp con solo email vacío
            onView(ViewMatchers.withId(R.id.tiet_email)).perform(ViewActions.clearText())
            onView(ViewMatchers.withId(R.id.tiet_password)).perform(ViewActions.replaceText("pppppp"))

            onView(ViewMatchers.withId(R.id.bRegister)).perform(ViewActions.click())

            onView(ViewMatchers.withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(
                ViewAssertions.matches(ViewMatchers.isDisplayed())
            )
            onView(ViewMatchers.withText(R.string.accept)).perform(ViewActions.click())

            onView(ViewMatchers.withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(
                ViewAssertions.doesNotExist()
            )
        }

    }

    @Test
    fun moreThan8CharactersLengthPassword_Test() {
        activityRule.scenario.use {
            onView(ViewMatchers.withId(R.id.tiet_email)).perform(ViewActions.replaceText("pepepp@outlook.com"))
            onView(ViewMatchers.withId(R.id.tiet_password)).perform(ViewActions.replaceText("1234567"))

            onView(ViewMatchers.withId(R.id.bRegister)).perform(ViewActions.click())
            onView(ViewMatchers.withText(R.string.password_length_insufficient))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            onView(ViewMatchers.withText(R.string.accept)).perform(ViewActions.click())

            onView(ViewMatchers.withText(R.string.password_length_insufficient))
                .check(ViewAssertions.doesNotExist())
        }
    }

    @Test
    fun swapToMainScreen() {
        activityRule.scenario.use {
            onView(withId(R.id.tiet_email)).perform(ViewActions.clearText())
            onView(withId(R.id.tiet_email)).perform(ViewActions.replaceText("pruebaregister@outlook.com"))

            onView(withId(R.id.tiet_password)).perform(ViewActions.clearText())
            onView(withId(R.id.tiet_password)).perform(ViewActions.replaceText("pppppppp"))

            onView(withId(R.id.bRegister)).perform(click())

            Thread.sleep(4000)

            Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
        }
    }

    @Test
    fun emailWithAnyUpperCaseLetter_ShouldDisplayPopUp() {
        activityRule.scenario.use {
            onView(withId(R.id.tiet_email)).perform(ViewActions.replaceText("HolaMundo@hotmail.com"))
            onView(withId(R.id.tiet_password)).perform(ViewActions.replaceText("pppppppp"))

            onView(withId(R.id.bRegister)).perform(click())

            onView(withText(R.string.no_upper_cases_at_email)).check(matches(isDisplayed()))
        }
    }
}