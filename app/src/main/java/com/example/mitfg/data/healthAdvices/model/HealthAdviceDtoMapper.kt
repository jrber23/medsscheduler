/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.healthAdvices.model

import com.example.mitfg.domain.model.HealthAdvice

/**
 * It converts an object HealthAdviceDto into the domain model object
 */
fun HealthAdviceDto.toDomain() : HealthAdvice =
    HealthAdvice(
        id = id,
        description = description,
        image = image,
        title = title
    )

/**
 * It converts a domain model object into an HealthAdviceDto object
 */
fun HealthAdvice.toDto() : HealthAdviceDto =
    HealthAdviceDto(
        id = id,
        description = description,
        image = image,
        title = title
    )