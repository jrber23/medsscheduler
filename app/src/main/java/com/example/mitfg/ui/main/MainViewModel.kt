package com.example.mitfg.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitfg.data.healthAdvices.HealthAdviceRepository
import com.example.mitfg.data.users.UserRepository
import com.example.mitfg.domain.model.HealthAdvice
import com.example.mitfg.domain.model.User
import com.example.mitfg.firebase.FirebaseTranslator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val healthAdviceRepository: HealthAdviceRepository,
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth,
    private val translator: FirebaseTranslator
) : ViewModel() {

    private val _healthAdvice = MutableStateFlow<HealthAdvice?>(null)
    val healthAdvice = _healthAdvice.asStateFlow()

    private val _user = MutableStateFlow<User>(User())
    val user = _user.asStateFlow()

    init {
        getHealthAdvice()
        getUserByEmail(auth.currentUser?.email.toString())
    }

    private fun getHealthAdvice() {
        viewModelScope.launch {
            healthAdviceRepository.getRandomHealthAdvice().fold(
                onSuccess = { advice ->
                    translateHealthAdvice(advice)

                    _healthAdvice.update { advice }
                },
                onFailure = { throwable -> Log.d("FAILURE", throwable.toString()) }
            )
        }
    }

    private fun getUserByEmail(email: String) {
        viewModelScope.launch {
            userRepository.getUserByEmail(email).fold(
                onSuccess = { foundUser ->
                    _user.update { foundUser!! }
                },
                onFailure =  { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }

    fun userIsDoctor() : Boolean {
        return this.user.value.isDoctor
    }

    fun translateHealthAdvice(advice: HealthAdvice?) {
        viewModelScope.launch {
                translator.translate(advice!!.title).fold(
                    onSuccess = {
                        _healthAdvice.update { advice ->
                            advice?.copy(title = it)
                        }
                    },
                    onFailure = { throwable ->
                        Log.d("FAILURE", throwable.toString())
                    }
                )

                translator.translate(advice.description).fold(
                    onSuccess = {
                        _healthAdvice.update { advice ->
                            advice?.copy(description = it)
                        }
                    },
                    onFailure = { throwable ->
                        Log.d("FAILURE", throwable.toString())
                    }
                )

        }
    }

}