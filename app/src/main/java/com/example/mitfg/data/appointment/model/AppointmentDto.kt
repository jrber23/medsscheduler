package com.example.mitfg.data.appointment.model

data class AppointmentDto(
    val emailPatient: String,
    val emailDoctor: String,
    val day: Int,
    val month: Int,
    val year: Int,
    val hour: Int,
    val minute: Int
) {
    constructor(): this("", "", 0,0,0,0,0)
}