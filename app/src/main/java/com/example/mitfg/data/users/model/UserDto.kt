package com.example.mitfg.data.users.model

import com.example.mitfg.domain.model.User

data class UserDto(
    val name: String,
    val surnames: String,
    val email: String,
    val password: String,
    @field:JvmField
    val isDoctor: Boolean,
    val patientsList: List<User>,
    val associatedDoctor: String?
) {
    constructor() : this("", "", "", "", false, emptyList(), null)
}


