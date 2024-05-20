/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.appointment

import com.example.mitfg.data.appointment.model.toDomain
import com.example.mitfg.data.appointment.model.toDto
import com.example.mitfg.domain.model.Appointment
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(
    private val appointmentDataSource: AppointmentDataSource
) : AppointmentRepository {
    override suspend fun addNewAppointment(appointment: Appointment) {
        appointmentDataSource.addNewAppointment(appointment.toDto())
    }

    override suspend fun getAppointmentsOfUser(email: String): Result<List<Appointment?>> =
        appointmentDataSource.getAppointmentsOfUser(email).fold(
            onSuccess = { list ->
                val resultList = mutableListOf<Appointment>()

                for (element in list) {
                    resultList.add(element!!.toDomain())
                }

                Result.success(resultList)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )

    override suspend fun getDoctorAppointments(email: String): Result<List<Appointment?>> =
        appointmentDataSource.getDoctorAppointments(email).fold(
            onSuccess = { list ->
                val resultList = mutableListOf<Appointment>()

                for (element in list) {
                    resultList.add(element!!.toDomain())
                }

                Result.success(resultList)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
    }