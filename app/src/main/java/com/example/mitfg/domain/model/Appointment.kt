package com.example.mitfg.domain.model

data class Appointment(
    val email: String,
    val day: Int,
    val month: Int,
    val year: Int,
    val hour: Int,
    val minute: Int
) {
    constructor(): this("",0,0,0,0,0)
}