/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

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

    fun addUser(name: String, surname: String, email: String, passwd: String, isDoctor: Boolean) {
        viewModelScope.launch {
            val newUser = User(name, surname, email, passwd, isDoctor, emptyList(), null)

            userRepository.addUser(newUser)
        }
    }


}