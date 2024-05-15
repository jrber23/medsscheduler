package com.example.mitfg.data.appointment

import com.example.mitfg.data.appointment.model.AppointmentDto

interface AppointmentDataSource {

    suspend fun addNewAppointment(appointmentDto: AppointmentDto)

    suspend fun getAppointmentsOfUser(email: String) : Result<List<AppointmentDto?>>

    suspend fun getDoctorAppointments(email: String) : Result<List<AppointmentDto?>>

}