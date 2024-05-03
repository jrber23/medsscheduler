package com.example.mitfg.data.users.model

import com.example.mitfg.domain.model.User

fun UserDto.toDomain() : User = User(name = name, surnames = surnames, email = email, password = password, isDoctor = isDoctor, patientsList = patientsList, associatedDoctor = associatedDoctor)

fun User.toDto() : UserDto = UserDto(name = name, surnames = surnames, email = email, password = password, isDoctor = isDoctor, patientsList = patientsList, associatedDoctor = associatedDoctor)