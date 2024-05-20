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
import androidx.test.espresso.Espresso.pressBack
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
import com.example.mitfg.ui.login.LoginActivity
import com.example.mitfg.ui.main.MainActivity
import com.example.mitfg.ui.register.RegisterActivity
import com.google.firebase.Firebase
import com.google.firebase.annotations.concurrent.UiThread
import com.google.firebase.auth.auth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class LoginActivityTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = activityScenarioRule<LoginActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()

        Firebase.auth.signOut()
    }

    @UiThread
    @Test
    fun showPopUp_Test() {
        activityRule.scenario.onActivity() { activity ->
            activity.showPopUp("Se deben cumplimentar todos los campos")
        }

        activityRule.scenario.use {
            onView(withText("Se deben cumplimentar todos los campos")).check(matches(isDisplayed()))
            onView(withText(R.string.accept)).perform(click())

            onView(withText("Se deben cumplimentar todos los campos")).check(doesNotExist())
        }
    }

    @Test
    @UiThread
    fun bothEmailAndPasswordEmpty_ShouldNotBeAllowed() {
        // PopUp con campos vacíos
        onView(withId(R.id.bLogin)).perform(click())

        onView(withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(matches(isDisplayed()))
        onView(withText(R.string.accept)).perform(click())

        onView(withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(doesNotExist())
    }

    @Test
    @UiThread
    fun passwordEmpty_ShouldNotBeAllowed() {
        // PopUp con solo password vacío
        onView(withId(R.id.tiet_email)).perform(clearText())
        onView(withId(R.id.tiet_email)).perform(replaceText("pepepp@outlook.com"))

        onView(withId(R.id.bLogin)).perform(click())

        onView(withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(matches(isDisplayed()))
        onView(withText(R.string.accept)).perform(click())

        onView(withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(doesNotExist())
    }

    @Test
    @UiThread
    fun emailEmpty_ShouldNotBeAllowed() {
        // PopUp con solo email vacío
        onView(withId(R.id.tiet_email)).perform(clearText())
        onView(withId(R.id.tiet_password)).perform(replaceText("pppppp"))

        onView(withId(R.id.bLogin)).perform(click())

        onView(withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(matches(isDisplayed()))
        onView(withText(R.string.accept)).perform(click())

        onView(withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(doesNotExist())
    }

    @UiThread
    @Test
    fun swapToMainScreen_Test() {
        onView(withId(R.id.tiet_email)).perform(clearText())
        onView(withId(R.id.tiet_email)).perform(replaceText("pepepp@outlook.com"))

        onView(withId(R.id.tiet_password)).perform(clearText())
        onView(withId(R.id.tiet_password)).perform(replaceText("pppppppp"))

        onView(withId(R.id.bLogin)).perform(click())
        pressBack()

        Thread.sleep(3000)

        Intents.intended(hasComponent(MainActivity::class.java.name))
    }

    @UiThread
    @Test
    fun swapToRegisterScreen_Test() {
        activityRule.scenario.use {
            onView(withId(R.id.tv_RegisterLink)).perform(click())
        }

        Thread.sleep(3000)
        Intents.intended(hasComponent(RegisterActivity::class.java.name))
    }

    @Test
    fun emailWithAnyUpperCaseLetter_ShouldDisplayPopUp() {
        activityRule.scenario.use {
            onView(withId(R.id.tiet_email)).perform(replaceText("HolaMundo@hotmail.com"))
            onView(withId(R.id.tiet_password)).perform(replaceText("pppppppp"))

            onView(withId(R.id.bLogin)).perform(click())

            onView(withText(R.string.no_upper_cases_at_email)).check(matches(isDisplayed()))
        }
    }

}