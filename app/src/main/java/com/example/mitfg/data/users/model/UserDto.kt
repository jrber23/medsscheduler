package com.example.mitfg.data.users.model

data class UserDto(
    val email: String,
    val password: String,
    @field:JvmField
    val isDoctor: Boolean
) {
    constructor() : this("", "", false)
}


