package com.example.mitfg.data.appointment

import com.example.mitfg.domain.model.Appointment

interface AppointmentRepository {

    suspend fun addNewAppointment(appointment: Appointment)

}