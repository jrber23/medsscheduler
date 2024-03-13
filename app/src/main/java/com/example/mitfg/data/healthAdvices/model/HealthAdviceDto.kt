package com.example.mitfg.data.healthAdvices.model

data class HealthAdviceDto(
    var title: String,
    var description: String,
    var image: String
) {
    constructor() : this("", "", "")
}
