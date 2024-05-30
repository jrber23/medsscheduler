/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.adverseEffects

import com.example.mitfg.data.adverseEffects.model.AdverseEffectDto

interface AdverseEffectDataSource {

    /**
     * Retrieves all storage adverse effects from the data source
     * @return an encapsulation of a list of adverse effects.
     */
    suspend fun getAllAdverseEffects() : Result<List<AdverseEffectDto?>>

}