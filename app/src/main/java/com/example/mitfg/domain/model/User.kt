package com.example.mitfg.domain.model

data class User(
    val name: String,
    val surnames: String,
    val email: String,
    val password: String,
    val isDoctor: Boolean,
    val patientsList: List<User>?,
    val associatedDoctor: User?
) {
    constructor() : this("", "", "", "", false, null, null)
}
