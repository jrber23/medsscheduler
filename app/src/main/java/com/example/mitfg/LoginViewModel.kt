package com.example.mitfg

import androidx.lifecycle.ViewModel
import com.example.mitfg.data.healthAdvices.HealthAdviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val healthAdviceRepository: HealthAdviceRepository
) : ViewModel() {

    val healthAdvice = healthAdviceRepository.getRandomHealthAdvice()

}