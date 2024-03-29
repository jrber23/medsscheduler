package com.example.mitfg.mappers

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mitfg.data.healthAdvices.model.HealthAdviceDto
import com.example.mitfg.data.healthAdvices.model.toDomain
import com.example.mitfg.data.healthAdvices.model.toDto
import com.example.mitfg.domain.model.HealthAdvice
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HealthAdviceMapperTest {

    @Test
    fun healthAdviceDtoToDomain_Test() {
        val healthAdviceDto = HealthAdviceDto("0", "MUÉVETE", "Es sano para tu bienestar", "Ruta de imagen")

        assertTrue(healthAdviceDto.toDomain() is HealthAdvice)
    }

    @Test
    fun healthAdviceToDto_Test() {
        val healthAdvice = HealthAdvice("0", "MUÉVETE", "Es sano para tu bienestar", "Ruta de imagen")

        assertTrue(healthAdvice.toDto() is HealthAdviceDto)
    }

}