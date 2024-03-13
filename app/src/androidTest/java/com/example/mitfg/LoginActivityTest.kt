package com.example.mitfg

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
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.mitfg.ui.LoginActivity
import com.example.mitfg.ui.MainActivity
import com.example.mitfg.ui.RegisterActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()

        Firebase.auth.signOut()
    }

    @Test
    fun showPopUp_Test() {
        val countDownLatch = CountDownLatch(1)

        activityRule.scenario.onActivity() { activity ->
            activity.showPopUp("Se deben cumplimentar todos los campos")

            countDownLatch.countDown()
        }

        countDownLatch.await()

        onView(withText("Se deben cumplimentar todos los campos")).check(matches(isDisplayed()))
        onView(withText(R.string.accept)).perform(click())

        onView(withText("Se deben cumplimentar todos los campos")).check(doesNotExist())
    }

    @Test
    fun anyFieldIsEmpty_Test() {
        // PopUp con campos vacíos
        onView(withId(R.id.bLogin)).perform(click())

        onView(withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(matches(isDisplayed()))
        onView(withText(R.string.accept)).perform(click())

        onView(withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(doesNotExist())

        // PopUp con solo password vacío
        onView(withId(R.id.tiet_email)).perform(clearText())
        onView(withId(R.id.tiet_email)).perform(replaceText("pepepp@outlook.com"))

        onView(withId(R.id.bLogin)).perform(click())

        onView(withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(matches(isDisplayed()))
        onView(withText(R.string.accept)).perform(click())

        onView(withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(doesNotExist())

        // PopUp con solo email vacío
        onView(withId(R.id.tiet_email)).perform(clearText())
        onView(withId(R.id.tiet_password)).perform(replaceText("pppppp"))

        onView(withId(R.id.bLogin)).perform(click())

        onView(withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(matches(isDisplayed()))
        onView(withText(R.string.accept)).perform(click())

        onView(withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(doesNotExist())
    }

    @Test
    fun swapToMainScreen_Test() {
        onView(withId(R.id.tiet_email)).perform(clearText())
        onView(withId(R.id.tiet_email)).perform(replaceText("pepepp@outlook.com"))

        onView(withId(R.id.tiet_password)).perform(clearText())
        onView(withId(R.id.tiet_password)).perform(replaceText("pppppp"))

        onView(withId(R.id.bLogin)).perform(click())

        Thread.sleep(3000)

        Intents.intended(hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun swapToRegisterScreen_Test() {
        onView(withId(R.id.tv_RegisterLink)).perform(click())

        Thread.sleep(3000)

        Intents.intended(hasComponent(RegisterActivity::class.java.name))
    }

}