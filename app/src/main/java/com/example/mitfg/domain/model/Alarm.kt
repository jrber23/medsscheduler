/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.domain.model

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