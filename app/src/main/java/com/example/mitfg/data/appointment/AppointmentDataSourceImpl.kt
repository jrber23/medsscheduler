/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.appointment

import com.example.mitfg.data.appointment.model.AppointmentDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Calendar.getInstance
import java.util.Date
import javax.inject.Inject

class AppointmentDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : AppointmentDataSource {
    override suspend fun addNewAppointment(appointmentDto: AppointmentDto) {
        val docRef = firestore.collection("appointments")

        val data = hashMapOf(
            "emailPatient" to appointmentDto.emailPatient,
            "emailDoctor" to appointmentDto.emailDoctor,
            "day" to appointmentDto.day,
            "month" to appointmentDto.month,
            "year" to appointmentDto.year,
            "hour" to appointmentDto.hour,
            "minute" to appointmentDto.minute,
            "timestamp" to Timestamp(Date())
        )

        docRef.add(data).await()
    }

    override suspend fun getAppointmentsOfUser(email: String) : Result<List<AppointmentDto?>> =
        withContext(Dispatchers.IO) {

            return@withContext try {
                val calendar = getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val docRef = firestore.collection("appointments")
                    .whereEqualTo("emailPatient", email)
                    .whereGreaterThanOrEqualTo("year", year)
                    .whereGreaterThanOrEqualTo("month", month)
                    .whereGreaterThanOrEqualTo("day", day)

                val list = mutableListOf<AppointmentDto?>()
                val snapshot = docRef.get().await()
                val documents = snapshot.documents

                for (element in documents) {
                    list.add(element.toObject<AppointmentDto?>())
                }

                Result.success(list)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }

    override suspend fun getDoctorAppointments(email: String): Result<List<AppointmentDto?>> =
        withContext(Dispatchers.IO) {
            val calendar = getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val docRef = firestore.collection("appointments")
                .whereEqualTo("emailDoctor", email)
                .whereGreaterThanOrEqualTo("year", year)
                .whereGreaterThanOrEqualTo("month", month)
                .whereGreaterThanOrEqualTo("day", day)

            return@withContext try {
                val list = mutableListOf<AppointmentDto?>()
                val snapshot = docRef.get().await()
                val documents = snapshot.documents

                for (element in documents) {
                    list.add(element.toObject<AppointmentDto?>())
                }

                Result.success(list)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }
    }