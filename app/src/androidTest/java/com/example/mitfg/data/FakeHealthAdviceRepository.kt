/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data

import com.example.mitfg.data.healthAdvices.HealthAdviceRepository
import com.example.mitfg.domain.model.HealthAdvice
import java.lang.Math.random
import javax.inject.Inject

class FakeHealthAdviceRepository @Inject constructor() : HealthAdviceRepository{

    private val fakeHealthAdvice = mutableListOf<HealthAdvice>(
        HealthAdvice("0", "¿Qué es Lorem Ipsum?", "img", "Titulo 1"),
        HealthAdvice("1", "¿De dónde viene?", "img", "Titulo 2"),
        HealthAdvice("2", "¿Por qué lo usamos?", "img", "Titulo 3"),
    )

    override suspend fun getRandomHealthAdvice(): Result<HealthAdvice?> {
        val randomNumber : Int = (random() * fakeHealthAdvice.size).toInt()
        val randomHealthAdvice = fakeHealthAdvice[randomNumber]

        return Result.success(randomHealthAdvice)
    }
}