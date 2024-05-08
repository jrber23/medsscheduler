package com.example.mitfg.data.appointment.model

import com.example.mitfg.domain.model.Appointment

fun AppointmentDto.toDomain() : Appointment = Appointment(email = email, day = day, month = month, year = year, hour = hour, minute = minute)

fun Appointment.toDto() : AppointmentDto = AppointmentDto(email = email, day = day, month = month, year = year, hour = hour, minute = minute)