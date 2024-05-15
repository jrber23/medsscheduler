package com.example.mitfg.data.appointment

import com.example.mitfg.domain.model.Appointment

interface AppointmentRepository {

    suspend fun addNewAppointment(appointment: Appointment)

    suspend fun getAppointmentsOfUser(email: String) : Result<List<Appointment?>>

    suspend fun getDoctorAppointments(email: String) : Result<List<Appointment?>>

}