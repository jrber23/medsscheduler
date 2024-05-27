/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.users.model

import com.example.mitfg.domain.model.User

fun UserDto.toDomain() : User = User(name = name, surnames = surnames, email = email, password = password, isDoctor = isDoctor, patientsList = patientsList, associatedDoctor = associatedDoctor, drugInteractions = drugInteractions)

fun User.toDto() : UserDto = UserDto(name = name, surnames = surnames, email = email, password = password, isDoctor = isDoctor, patientsList = patientsList, associatedDoctor = associatedDoctor, drugInteractions = drugInteractions)