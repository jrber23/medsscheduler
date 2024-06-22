/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.appointment

import android.util.Log
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

class AppointmentFirestore @Inject constructor(
    private val firestore: FirebaseFirestore
) : AppointmentDataSource {

    /**
     * Adds a new appointment to a data source
     * @param appointmentDto The object with all the data to be storage.
     */
    override suspend fun addNewAppointment(appointmentDto: AppointmentDto) {
        // Looks for the collection with the given name
        val docRef = firestore.collection("appointments")

        // Maps all the DTO data to the right document field
        val dataToAdd = hashMapOf(
            "emailPatient" to appointmentDto.emailPatient,
            "emailDoctor" to appointmentDto.emailDoctor,
            "day" to appointmentDto.day,
            "month" to appointmentDto.month,
            "year" to appointmentDto.year,
            "hour" to appointmentDto.hour,
            "minute" to appointmentDto.minute,
            "timestamp" to Timestamp(Date())
        )

        // The new appointment is added
        docRef.add(dataToAdd).await()
    }

    /**
     * Retrieves all apointments of the user with the given email address
     * @param email the patient email address
     * @return an encapsulation of a list of appointments
     */
    override suspend fun getAppointmentsOfUser(email: String) : Result<List<AppointmentDto?>> =
        withContext(Dispatchers.IO) {

            return@withContext try {

                // Gets the current year, month and day
                val calendar = getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                // Looks for all the appointments after the current date
                val docRef = firestore.collection("appointments")
                    .whereEqualTo("emailPatient", email)
                    .whereGreaterThanOrEqualTo("year", year)
                    .whereGreaterThanOrEqualTo("month", month)
                    .whereGreaterThanOrEqualTo("day", day)

                val result = mutableListOf<AppointmentDto?>()
                val snapshot = docRef.get().await()
                val documents = snapshot.documents

                // Every retrieved document is mapped to a DTO object and added to the result list
                for (element in documents) {
                    result.add(element.toObject<AppointmentDto?>())
                }

                Result.success(result)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }

    /**
     * Retrieves all apointments of a doctor with the given email address
     * @param email the doctor email address
     * @return an encapsulation of a list of appointments
     */
    override suspend fun getDoctorAppointments(email: String): Result<List<AppointmentDto?>> =
        withContext(Dispatchers.IO) {

            // Gets the current year, month and day
            val calendar = getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Looks for all the appointments after the current date
            val docRef = firestore.collection("appointments")
                .whereEqualTo("emailDoctor", email)
                .whereGreaterThanOrEqualTo("year", year)
                .whereGreaterThanOrEqualTo("month", month)
                .whereGreaterThanOrEqualTo("day", day)

            return@withContext try {
                val result = mutableListOf<AppointmentDto?>()
                val snapshot = docRef.get().await()
                val documents = snapshot.documents

                // Every retrieved document is mapped to a DTO object and added to the result list
                for (element in documents) {
                    result.add(element.toObject<AppointmentDto?>())
                }

                Result.success(result)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }

    /**
     *
     */
    override suspend fun checkAppointment(
        appointmentDto: AppointmentDto
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            var startMinute = appointmentDto.minute - 30
            var endMinute = appointmentDto.minute + 30

            var startHour = appointmentDto.hour
            var endHour = appointmentDto.hour

            if (startMinute < 0) {
                startMinute += 60
                startHour -= 1
            }

            if (endMinute >= 60) {
                endMinute -= 60
                endHour += 1
            }

            if (startHour < 0) {
                startHour = 23
            }

            if (endHour >= 24) {
                endHour = 0
            }

            return@withContext try {
                // Looks for all the appointments after the current date
                val startQuery = firestore.collection("appointments")
                    .whereEqualTo("year", appointmentDto.year)
                    .whereEqualTo("month", appointmentDto.month)
                    .whereEqualTo("day", appointmentDto.day)
                    .whereEqualTo("hour", startHour)
                    .whereGreaterThanOrEqualTo("minute", startMinute)

                val endQuery = firestore.collection("appointments")
                    .whereEqualTo("year", appointmentDto.year)
                    .whereEqualTo("month", appointmentDto.month)
                    .whereEqualTo("day", appointmentDto.day)
                    .whereEqualTo("hour", endHour)
                    .whereLessThanOrEqualTo("minute", endMinute)


                    val startSnapshot = startQuery.get().await()
                    val endSnapshot = endQuery.get().await()

                    Log.d("START_QUERY", startSnapshot.documents.size.toString())
                    Log.d("END_QUERY", endSnapshot.documents.size.toString())

                    val result = startSnapshot.documents.isNotEmpty() || endSnapshot.documents.isNotEmpty()

                    Result.success(result)
                } catch (e: FirebaseFirestoreException) {
                    Result.failure(e)
                }
            }
        }