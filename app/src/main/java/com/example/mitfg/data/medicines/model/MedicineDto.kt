/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.medicines.model

data class MedicineDto(
    var id: String,
    var name: String,
    var description: String,
    var adverseEffects: List<String>
) {
    constructor() : this("", "", "", emptyList())
}