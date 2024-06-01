/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.adverseEffects.model

/**
 * Data Transfer Object of an adverse effect.
 * It includes an ID an a name to identify each one.
 */
data class AdverseEffectDto(
    var id: String = "",
    val name: String = ""
) {

}