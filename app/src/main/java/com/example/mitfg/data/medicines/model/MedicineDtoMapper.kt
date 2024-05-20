/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.medicines.model

import com.example.mitfg.domain.model.Medicine

fun MedicineDto.toDomain() : Medicine = Medicine(id = id, name = name, description = description, adverseEffects = adverseEffects)

fun Medicine.toDto() : MedicineDto = MedicineDto(id = id, name = name, description = description, adverseEffects = adverseEffects)