package com.example.mitfg.data.instructionAlarm.model

import com.example.mitfg.domain.model.Alarm

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