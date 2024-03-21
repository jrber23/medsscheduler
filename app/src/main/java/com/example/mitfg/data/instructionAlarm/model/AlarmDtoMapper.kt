package com.example.mitfg.data.instructionAlarm.model

import com.example.mitfg.domain.model.Alarm

fun AlarmDto.toDomain() : Alarm = Alarm(
    id = id,
    medicineName = medicineName,
    quantity = quantity,
    frequency = frequency,
    hourStart = hourStart,
    minuteStart = minuteStart
)

fun Alarm.toDto() : AlarmDto = AlarmDto(
    id = id,
    medicineName = medicineName,
    quantity = quantity,
    frequency = frequency,
    hourStart = hourStart,
    minuteStart = minuteStart
)