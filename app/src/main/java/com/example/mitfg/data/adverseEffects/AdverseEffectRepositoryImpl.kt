package com.example.mitfg.data.adverseEffects

import com.example.mitfg.data.adverseEffects.model.toDomain
import com.example.mitfg.domain.model.AdverseEffect
import javax.inject.Inject

class AdverseEffectRepositoryImpl @Inject constructor(
    private val adverseEffectDataSource: AdverseEffectDataSource
) : AdverseEffectRepository {
    override suspend fun getAllAdverseEffects(): Result<List<AdverseEffect?>> =
        adverseEffectDataSource.getAllAdverseEffects().fold(
            onSuccess = { list ->
                Result.success(list.map {
                    it?.toDomain()
                })
            },
            onFailure = {
                throwable -> Result.failure(throwable)
            }
        )


}