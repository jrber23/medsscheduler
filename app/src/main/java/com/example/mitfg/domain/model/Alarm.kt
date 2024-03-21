package com.example.mitfg.domain.model

data class Alarm(
    val id: Long,
    val medicineName: String,
    val quantity: Int,
    val frequency: Long,
    val hourStart: Int,
    val minuteStart: Int
)