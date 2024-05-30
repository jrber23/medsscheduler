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

interface AppointmentDataSource {

    /**
     * Adds a new appointment to a data source
     * @param appointmentDto The object with all the data to be storage.
     */
    suspend fun addNewAppointment(appointmentDto: AppointmentDto)

    /**
     * Retrieves all apointments of the user with the given email address
     * @param email the patient email address
     * @return an encapsulation of a list of appointments
     */
    suspend fun getAppointmentsOfUser(email: String) : Result<List<AppointmentDto?>>

    /**
     * Retrieves all apointments of a doctor with the given email address
     * @param email the doctor email address
     * @return an encapsulation of a list of appointments
     */
    suspend fun getDoctorAppointments(email: String) : Result<List<AppointmentDto?>>

}