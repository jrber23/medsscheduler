/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.domain.model

data class User(
    val name: String,
    val surnames: String,
    val email: String,
    val password: String,
    val isDoctor: Boolean,
    val patientsList: List<User>,
    val associatedDoctor: String?
) {
    constructor() : this("", "", "", "", false, emptyList(), null)
}
