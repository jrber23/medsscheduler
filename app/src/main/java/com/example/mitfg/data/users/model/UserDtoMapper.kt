package com.example.mitfg.data.users.model

import com.example.mitfg.domain.model.User

fun UserDto.toDomain() : User = User(email = email, password = password, isDoctor = isDoctor)

fun User.toDto() : UserDto = UserDto(email = email, password = password, isDoctor = isDoctor)