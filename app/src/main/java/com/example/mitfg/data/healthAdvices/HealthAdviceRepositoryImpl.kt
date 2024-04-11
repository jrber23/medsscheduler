package com.example.mitfg.data.healthAdvices

import com.example.mitfg.data.healthAdvices.model.toDomain
import com.example.mitfg.domain.model.HealthAdvice
import com.example.mitfg.firebase.FirebaseTranslator
import javax.inject.Inject
import kotlin.random.Random

class HealthAdviceRepositoryImpl @Inject constructor(
    private val healthAdviceDataSource: HealthAdviceDataSource,
    private val translator: FirebaseTranslator
) : HealthAdviceRepository {
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