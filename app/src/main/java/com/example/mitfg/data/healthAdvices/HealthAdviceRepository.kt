/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.healthAdvices

import com.example.mitfg.domain.model.HealthAdvice

interface HealthAdviceRepository {

    suspend fun getRandomHealthAdvice(): Result<HealthAdvice?>

}