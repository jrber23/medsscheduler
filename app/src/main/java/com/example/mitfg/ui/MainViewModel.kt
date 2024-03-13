package com.example.mitfg.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitfg.data.healthAdvices.HealthAdviceRepository
import com.example.mitfg.domain.model.HealthAdvice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val healthAdviceRepository: HealthAdviceRepository
) : ViewModel() {

    init {
        getHealthAdvice()
    }

    private val _healthAdvice = MutableStateFlow<HealthAdvice?>(null)
    val healthAdvice = _healthAdvice.asStateFlow()

    private fun getHealthAdvice() {
        viewModelScope.launch {
            healthAdviceRepository.getRandomHealthAdvice().fold(
                onSuccess = { advice ->
                    _healthAdvice.update { advice }
                },
                onFailure = { throwable -> Log.d("FAILURE", throwable.toString()) }
            )
        }
    }

}