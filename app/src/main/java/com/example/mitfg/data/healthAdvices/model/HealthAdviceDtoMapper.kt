package com.example.mitfg.data.healthAdvices.model

import com.example.mitfg.domain.model.HealthAdvice

fun HealthAdviceDto.toDomain() : HealthAdvice = HealthAdvice(description = description, image = image, title = title)

fun HealthAdvice.toDto() : HealthAdviceDto = HealthAdviceDto(description = description, image = image, title = title)