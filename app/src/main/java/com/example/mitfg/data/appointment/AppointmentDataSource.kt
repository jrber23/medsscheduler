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

    suspend fun addNewAppointment(appointmentDto: AppointmentDto)

    suspend fun getAppointmentsOfUser(email: String) : Result<List<AppointmentDto?>>

    suspend fun getDoctorAppointments(email: String) : Result<List<AppointmentDto?>>

}