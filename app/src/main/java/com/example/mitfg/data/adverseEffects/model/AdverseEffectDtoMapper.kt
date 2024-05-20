/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.adverseEffects.model

import com.example.mitfg.domain.model.AdverseEffect

fun AdverseEffectDto.toDomain() : AdverseEffect = AdverseEffect(id = id, name = name)

fun AdverseEffect.toDto() : AdverseEffectDto = AdverseEffectDto(id = id, name = name)