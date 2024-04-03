package com.example.mitfg.data.adverseEffects

import com.example.mitfg.data.adverseEffects.model.AdverseEffectDto

interface AdverseEffectDataSource {

    suspend fun getAllAdverseEffects() : Result<List<AdverseEffectDto?>>

}