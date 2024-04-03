package com.example.mitfg.data.adverseEffects.model

import com.example.mitfg.domain.model.AdverseEffect

fun AdverseEffectDto.toDomain() : AdverseEffect = AdverseEffect(id = id, name = name)

fun AdverseEffect.toDto() : AdverseEffectDto = AdverseEffectDto(id = id, name = name)