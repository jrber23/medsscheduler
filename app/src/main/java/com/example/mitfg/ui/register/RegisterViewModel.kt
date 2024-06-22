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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The RegisterActivity's view model that handles every data shown by the activity.
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _newUser : MutableStateFlow<User> = MutableStateFlow<User>(User())
    val newUser = _newUser.asStateFlow()

    fun registerUser(name: String, surname: String, email: String, passwd: String, isDoctor: Boolean) {
        val newUser = User(name, surname, email, passwd, isDoctor, emptyList(), null, emptyList())

        _newUser.update { newUser }
    }

    /**
     * Adds a new user to the repository.
     *
     * @param name The user's first name.
     * @param surname The user's surname.
     * @param email The user's email address.
     * @param passwd The user's password.
     * @param isDoctor A boolean indicating whether the user is a doctor.
     */
    fun addUser() {
        viewModelScope.launch {
            userRepository.addUser(_newUser.value)
        }
    }
}