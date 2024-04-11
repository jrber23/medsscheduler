package com.example.mitfg.domain.model

data class User(
    val email: String,
    val password: String,
    val isDoctor: Boolean
) {
    constructor() : this("", "", false)
}
