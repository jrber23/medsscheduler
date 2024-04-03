package com.example.mitfg.data.adverseEffects

import com.example.mitfg.domain.model.AdverseEffect

interface AdverseEffectRepository {

    suspend fun getAllAdverseEffects(): Result<List<AdverseEffect?>>

}