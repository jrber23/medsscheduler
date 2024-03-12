package com.example.mitfg.data.healthAdvices

import com.example.mitfg.domain.model.HealthAdvice

interface HealthAdviceRepository {

    fun getRandomHealthAdvice(): HealthAdvice?

}