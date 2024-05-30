/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.domain.model

/**
 * A health advice contains an ID, a description an URL of an image and a title
 */
data class HealthAdvice(
    val id: String,
    var description: String,
    val image: String,
    var title: String
)
