package com.example.mitfg.data.appointment.model

import com.example.mitfg.domain.model.Appointment

fun AppointmentDto.toDomain() : Appointment = Appointment(emailPatient = emailPatient, emailDoctor = emailDoctor, day = day, month = month, year = year, hour = hour, minute = minute)

fun Appointment.toDto() : AppointmentDto = AppointmentDto(emailPatient = emailPatient, emailDoctor = emailDoctor, day = day, month = month, year = year, hour = hour, minute = minute)