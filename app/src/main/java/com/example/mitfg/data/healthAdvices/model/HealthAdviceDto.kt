package com.example.mitfg.data.healthAdvices.model

data class HealthAdviceDto(
    val title: String,
    val description: String,
    val image: String
) {
    constructor() : this("", "", "")
}
