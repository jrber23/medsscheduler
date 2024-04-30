package com.example.mitfg.data.adverseEffects

import com.example.mitfg.domain.model.AdverseEffect
import com.example.mitfg.domain.model.Medicine

interface AdverseEffectRepository {

    suspend fun getAllAdverseEffects(): Result<List<AdverseEffect?>>



}