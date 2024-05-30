/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.medicines.model

/**
 * Data Transfer Object of a medicine.
 * It includes an ID, a name, a description, and a list
 * of associated adverse effects.
 */
data class MedicineDto(
    var id: String,
    var name: String,
    var description: String,
    var adverseEffects: List<String>
)