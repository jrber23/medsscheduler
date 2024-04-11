package com.example.mitfg.ui.login

import androidx.lifecycle.ViewModel
import com.example.mitfg.data.users.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {




}