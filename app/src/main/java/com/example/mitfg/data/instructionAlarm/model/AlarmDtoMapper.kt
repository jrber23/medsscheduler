/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.instructionAlarm.model

import com.example.mitfg.domain.model.Alarm

/**
 * It converts an object AlarmDto into the domain model object
 */
fun AlarmDto.toDomain() : Alarm = Alarm(
    id = id,
    medicineName = medicineName,
    medicinePresentation = medicinePresentation,
    quantity = quantity,
    frequency = frequency,
    hourStart = hourStart,
    minuteStart = minuteStart,
    totalDosages = totalDosages,
    takenDosages = takenDosages
)

/**
 * It converts a domain model object into an AlarmDto object
 */
fun Alarm.toDto() : AlarmDto = AlarmDto(
    id = id,
    medicineName = medicineName,
    medicinePresentation = medicinePresentation,
    quantity = quantity,
    frequency = frequency,
    hourStart = hourStart,
    minuteStart = minuteStart,
    totalDosages = totalDosages,
    takenDosages = takenDosages
)