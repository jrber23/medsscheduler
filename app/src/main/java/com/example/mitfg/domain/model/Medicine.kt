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
 * A medicine contains an ID, a name, a description and a list of possible adverse effects
 */
data class Medicine(
    val id: String,
    val name: String,
    val description: String,
    val adverseEffects: List<String>
)