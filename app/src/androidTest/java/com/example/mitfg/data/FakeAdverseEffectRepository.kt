package com.example.mitfg.data

import com.example.mitfg.data.adverseEffects.AdverseEffectRepository
import com.example.mitfg.domain.model.AdverseEffect
import javax.inject.Inject

class FakeAdverseEffectRepository @Inject constructor() : AdverseEffectRepository {

    private val fakeAdverseEffect = mutableListOf<AdverseEffect?>(
        AdverseEffect("0", "Efecto 1"),
        AdverseEffect("1", "Efecto 2"),
        AdverseEffect("2", "Efecto 3"),
    )

    override suspend fun getAllAdverseEffects(): Result<List<AdverseEffect?>> {
        return Result.success(fakeAdverseEffect)
    }


}