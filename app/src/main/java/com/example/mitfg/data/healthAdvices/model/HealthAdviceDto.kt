/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.healthAdvices.model

data class HealthAdviceDto(
    var id: String,
    var title: String,
    var description: String,
    var image: String
) {
    constructor() : this("","", "", "")
}
