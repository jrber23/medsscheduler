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

    /**
     * Adds a new appointment to a data source
     * @param appointment The object with all the data to be storage. It's mapped to a DTO object.
     */
    override suspend fun addNewAppointment(appointment: Appointment) {
        appointmentDataSource.addNewAppointment(appointment.toDto())
    }

    /**
     * Retrieves all apointments of the user with the given email address
     * @param email the patient email address
     * @return an encapsulation of a list of appointments. It's mapped to a domain model object.
     */
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

    /**
     * Retrieves all apointments of a doctor with the given email address
     * @param email the doctor email address
     * @return an encapsulation of a list of appointments. It's mapped to a domain model object.
     */
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

    override suspend fun checkAppointment(
        appointment: Appointment
    ): Result<Boolean> =
        appointmentDataSource.checkAppointment(appointment.toDto()).fold(
            onSuccess = { existsAnAppointment ->
                Result.success(existsAnAppointment)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
}