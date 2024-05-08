package com.example.mitfg.data.appointment.model

data class AppointmentDto(
    val email: String,
    val day: Int,
    val month: Int,
    val year: Int,
    val hour: Int,
    val minute: Int
) {
    constructor(): this("",0,0,0,0,0)
}