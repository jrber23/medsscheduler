package com.example.mitfg.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitfg.data.users.UserRepository
import com.example.mitfg.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun addUser(email: String, passwd: String, isDoctor: Boolean) {
        viewModelScope.launch {
            val newUser = User(email, passwd, isDoctor)

            userRepository.addUser(newUser)
        }
    }


}