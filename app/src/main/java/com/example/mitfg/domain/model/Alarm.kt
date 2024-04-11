package com.example.mitfg.domain.model

data class Alarm(
    val id: Long,
    val medicineName: String,
    val medicinePresentation: String,
    val quantity: String,
    val frequency: Long,
    val hourStart: Int,
    val minuteStart: Int
)