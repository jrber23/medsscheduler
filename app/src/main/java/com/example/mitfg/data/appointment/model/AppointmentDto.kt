/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.appointment.model

/**
 * Data Transfer Object of an appointment.
 * It includes an both emails of patient and the doctor,
 * and the day, month, year, hour and minute of the appointment.
 */
data class AppointmentDto(
    val emailPatient: String = "",
    val emailDoctor: String = "",
    val day: Int = -1,
    val month: Int = -1,
    val year: Int = -1,
    val hour: Int = -1,
    val minute: Int = -1
) {
    // constructor() : this("", "",0,0,0,0,0)
}