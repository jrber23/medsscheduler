package com.example.mitfg.data.healthAdvices


import com.example.mitfg.data.healthAdvices.model.HealthAdviceDto

interface HealthAdviceDataSource {

    fun getRandomHealthAdvice(): HealthAdviceDto?

}