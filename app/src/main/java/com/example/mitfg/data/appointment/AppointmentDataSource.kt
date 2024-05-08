package com.example.mitfg.data.appointment

import com.example.mitfg.data.appointment.model.AppointmentDto

interface AppointmentDataSource {

    suspend fun addNewAppointment(appointmentDto: AppointmentDto)

}