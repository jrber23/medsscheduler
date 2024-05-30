/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.users.model

import com.example.mitfg.domain.model.User

/**
 * Data Transfer Object of a user.
 */
data class UserDto(
    val name: String,
    val surnames: String,
    val email: String,
    val password: String,
    @field:JvmField
    val isDoctor: Boolean,
    val patientsList: List<User>,
    val associatedDoctor: String?,
    val drugInteractions: List<String>
) {
    constructor() : this("", "", "", "", false, emptyList(), null, emptyList())
}


