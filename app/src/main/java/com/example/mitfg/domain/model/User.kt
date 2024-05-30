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
 * A user has data that shows if it's doctor or not, the associated doctor and a list of potential drug interactions for the user
 */
data class User(
    val name: String,
    val surnames: String,
    val email: String,
    val password: String,
    val isDoctor: Boolean,
    val patientsList: List<User>,
    val associatedDoctor: String?,
    val drugInteractions: List<String>
) {
    constructor() : this("", "", "", "", false, emptyList(), null, emptyList())
}
