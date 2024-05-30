/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.healthAdvices

import com.example.mitfg.data.healthAdvices.model.toDomain
import com.example.mitfg.domain.model.HealthAdvice
import javax.inject.Inject
import kotlin.random.Random

class HealthAdviceRepositoryImpl @Inject constructor(
    private val healthAdviceDataSource: HealthAdviceDataSource
) : HealthAdviceRepository {

    /**
     * Retrieves all the health advices storage in the data source
     * @return an encapsulation of a list of health advices. It's mapped to a domain model object
     */
    override suspend fun getRandomHealthAdvice(): Result<HealthAdvice?> =
        healthAdviceDataSource.getAllHealthAdvices().fold(
            onSuccess = { list ->
                val randomIndex = Random.nextInt(list.size)
                val randomDocument = list[randomIndex]

                Result.success(randomDocument!!.toDomain())

            },
            onFailure = { throwable -> Result.failure(throwable)}
        )
}