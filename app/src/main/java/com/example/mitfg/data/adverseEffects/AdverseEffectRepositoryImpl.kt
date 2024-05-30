/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.adverseEffects

import com.example.mitfg.data.adverseEffects.model.toDomain
import com.example.mitfg.domain.model.AdverseEffect
import javax.inject.Inject

class AdverseEffectRepositoryImpl @Inject constructor(
    private val adverseEffectDataSource: AdverseEffectDataSource
) : AdverseEffectRepository {

    /**
     * Retrieves all storage adverse effects from the data source
     * @return an encapsulation of a list of adverse effects. It's mapped to an domain model object
     */
    override suspend fun getAllAdverseEffects(): Result<List<AdverseEffect?>> =
        adverseEffectDataSource.getAllAdverseEffects().fold(
            onSuccess = { retrievedList ->
                Result.success(retrievedList.map { currentAdverseEffect ->
                    currentAdverseEffect?.toDomain()
                })
            },
            onFailure = {
                throwable -> Result.failure(throwable)
            }
        )


}