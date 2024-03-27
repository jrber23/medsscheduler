package com.example.mitfg.viewModelTesting

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mitfg.data.healthAdvices.HealthAdviceDataSourceImpl
import com.example.mitfg.data.healthAdvices.HealthAdviceRepositoryImpl
import com.example.mitfg.domain.model.HealthAdvice
import com.example.mitfg.ui.main.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

    private lateinit var viewModel : MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(HealthAdviceRepositoryImpl(HealthAdviceDataSourceImpl(Firebase.firestore)))
    }

    @Test
    fun getHealthAdvice_Test() {
        val healthAdvice = viewModel.healthAdvice

        assertTrue(healthAdvice.value is HealthAdvice?)
        assertNotNull(healthAdvice)
    }

}