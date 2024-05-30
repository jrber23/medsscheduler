/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.domain.model

/**
 * All the data that contains the alarm. It's an ID, the medicine name associated to the alarm,
 * its presentation (pills, packets...), the quantity, the starting hour and minute, the total
 * triggered alarms and the number of taken dosages among the total.
 */
data class Alarm(
    val id: Long,
    val medicineName: String,
    val medicinePresentation: String,
    val quantity: String,
    val frequency: Long,
    val hourStart: Int,
    val minuteStart: Int,
    var totalDosages: Int,
    val takenDosages: Int
)