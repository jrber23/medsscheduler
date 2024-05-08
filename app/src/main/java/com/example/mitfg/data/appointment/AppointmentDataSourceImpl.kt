package com.example.mitfg.data.appointment

import com.example.mitfg.data.appointment.model.AppointmentDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppointmentDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : AppointmentDataSource {
    override suspend fun addNewAppointment(appointmentDto: AppointmentDto) {
        val docRef = firestore.collection("appointments")

        val data = hashMapOf(
            "email" to appointmentDto.email,
            "day" to appointmentDto.day,
            "month" to appointmentDto.month,
            "year" to appointmentDto.year,
            "hour" to appointmentDto.hour,
            "minute" to appointmentDto.minute
        )

        docRef.add(data).await()
    }


}