package com.example.mitfg.viewModelTesting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mitfg.data.FakeHealthAdviceRepository
import com.example.mitfg.data.FakeUserRepository
import com.example.mitfg.domain.model.User
import com.example.mitfg.firebase.FirebaseTranslator
import com.example.mitfg.ui.main.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

    private lateinit var healthAdviceRepository: FakeHealthAdviceRepository
    private lateinit var userRepository: FakeUserRepository
    private lateinit var auth: FirebaseAuth
    private lateinit var translator: FirebaseTranslator

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testUser = User("nach8@yahoo.com", "holamundo", true)

    @Before
    fun setUp() {
        healthAdviceRepository = FakeHealthAdviceRepository()
        userRepository = FakeUserRepository()
        auth = Firebase.auth
        translator = FirebaseTranslator()

        auth.signInWithEmailAndPassword(testUser.email, testUser.password)
    }

    @Test
    fun getUserByEmail_Test() = runTest {
        backgroundScope.launch {
            val mainViewModel = MainViewModel(
                healthAdviceRepository = healthAdviceRepository,
                userRepository = userRepository,
                auth = auth,
                translator = translator
            )

            assertTrue(mainViewModel.userIsDoctor())
            assertEquals(mainViewModel.user.value.email, testUser.email)
        }
    }

    @Test
    fun getHealthAdvice_Test() = runTest {
        backgroundScope.launch {
            val mainViewModel = MainViewModel(
                healthAdviceRepository = healthAdviceRepository,
                userRepository = userRepository,
                auth = auth,
                translator =translator
            )

            backgroundScope.launch {

                mainViewModel.healthAdvice.collect { healthAdvice ->
                    assertNotNull(healthAdvice!!.id)
                }
            }
        }
    }

}