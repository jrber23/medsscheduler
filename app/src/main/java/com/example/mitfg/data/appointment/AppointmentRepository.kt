/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.appointment

import com.example.mitfg.domain.model.Appointment

interface AppointmentRepository {

    /**
     * Adds a new appointment to a data source
     * @param appointment The object with all the data to be storage. It's mapped to a DTO object.
     */
    suspend fun addNewAppointment(appointment: Appointment)

    /**
     * Retrieves all apointments of the user with the given email address
     * @param email the patient email address
     * @return an encapsulation of a list of appointments. It's mapped to a domain model object.
     */
    suspend fun getAppointmentsOfUser(email: String) : Result<List<Appointment?>>

    /**
     * Retrieves all apointments of a doctor with the given email address
     * @param email the doctor email address
     * @return an encapsulation of a list of appointments. It's mapped to a domain model object.
     */
    suspend fun getDoctorAppointments(email: String) : Result<List<Appointment?>>

}