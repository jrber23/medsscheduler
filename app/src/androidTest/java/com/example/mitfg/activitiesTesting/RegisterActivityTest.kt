package com.example.mitfg.activitiesTesting

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.mitfg.R
import com.example.mitfg.ui.main.MainActivity
import com.example.mitfg.ui.register.RegisterActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch


@RunWith(AndroidJUnit4::class)
@LargeTest
class RegisterActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @Test
    fun showPopUp_Test() {
        val countDownLatch = CountDownLatch(1)

        activityRule.scenario.onActivity() { activity ->
            activity.showPopUp("Se deben cumplimentar todos los campos")

            countDownLatch.countDown()
        }

        countDownLatch.await()

        onView(ViewMatchers.withText("Se deben cumplimentar todos los campos"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(R.string.accept)).perform(ViewActions.click())

        onView(ViewMatchers.withText("Se deben cumplimentar todos los campos"))
            .check(ViewAssertions.doesNotExist())
    }

    @Test
    fun anyFieldIsEmpty_Test() {
        // PopUp con campos vacíos
        onView(ViewMatchers.withId(R.id.bRegister)).perform(ViewActions.click())

        onView(ViewMatchers.withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        onView(ViewMatchers.withText(R.string.accept)).perform(ViewActions.click())

        onView(ViewMatchers.withText(R.string.emptyFieldsLoginOrRegisterMessage)).check(
            ViewAssertions.doesNotExist()
        )

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

    @Test
    fun moreThan8CharactersLengthPassword_Test() {
        onView(ViewMatchers.withId(R.id.tiet_email)).perform(ViewActions.replaceText("pepepp@outlook.com"))
        onView(ViewMatchers.withId(R.id.tiet_password)).perform(ViewActions.replaceText("1234567"))

        onView(ViewMatchers.withId(R.id.bRegister)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.password_length_insufficient))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(R.string.accept)).perform(ViewActions.click())

        onView(ViewMatchers.withText(R.string.password_length_insufficient))
            .check(ViewAssertions.doesNotExist())

    }

    @Test
    fun swapToMainScreen() {
        onView(ViewMatchers.withId(R.id.tiet_email)).perform(ViewActions.clearText())
        onView(ViewMatchers.withId(R.id.tiet_email)).perform(ViewActions.replaceText("pruebaregister@outlook.com"))

        onView(ViewMatchers.withId(R.id.tiet_password)).perform(ViewActions.clearText())
        onView(ViewMatchers.withId(R.id.tiet_password)).perform(ViewActions.replaceText("pppppppp"))

        onView(ViewMatchers.withId(R.id.bRegister)).perform(ViewActions.click())

        Thread.sleep(4000)

        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
    }

    @After
    fun tearDown() {
        Intents.release()
    }


}