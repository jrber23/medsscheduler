/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.healthAdvices


import com.example.mitfg.data.healthAdvices.model.HealthAdviceDto

interface HealthAdviceDataSource {

    /**
     * Retrieves all the health advices storage in the data source
     * @return an encapsulation of a list of health advices
     */
    suspend fun getAllHealthAdvices(): Result<List<HealthAdviceDto?>>

}