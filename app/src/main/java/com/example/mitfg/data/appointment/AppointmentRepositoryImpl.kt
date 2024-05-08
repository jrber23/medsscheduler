package com.example.mitfg.data.appointment

import com.example.mitfg.data.appointment.model.toDto
import com.example.mitfg.domain.model.Appointment
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(
    private val appointmentDataSource: AppointmentDataSource
) : AppointmentRepository {
    override suspend fun addNewAppointment(appointment: Appointment) {
        appointmentDataSource.addNewAppointment(appointment.toDto())
    }
}