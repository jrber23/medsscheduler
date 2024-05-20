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

fun HealthAdviceDto.toDomain() : HealthAdvice = HealthAdvice(id = id, description = description, image = image, title = title)

fun HealthAdvice.toDto() : HealthAdviceDto = HealthAdviceDto(id = id, description = description, image = image, title = title)