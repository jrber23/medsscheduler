package com.example.mitfg.data.healthAdvices

import android.util.Log
import com.example.mitfg.data.healthAdvices.model.toDomain
import com.example.mitfg.domain.model.HealthAdvice
import javax.inject.Inject

class HealthAdviceRepositoryImpl @Inject constructor(
    private val healthAdviceDataSource: HealthAdviceDataSource
) : HealthAdviceRepository {
    override fun getRandomHealthAdvice(): HealthAdvice? {
        Log.d("Campeones", healthAdviceDataSource.getRandomHealthAdvice()?.toDomain().toString())

        return healthAdviceDataSource.getRandomHealthAdvice()?.toDomain()
    }
}