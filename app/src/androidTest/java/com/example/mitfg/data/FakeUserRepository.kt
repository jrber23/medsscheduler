/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data

import com.example.mitfg.data.users.UserRepository
import com.example.mitfg.domain.model.User
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : UserRepository {

    private val fakeUsers = mutableListOf<User>(
        User("José Ramón", "Bermejo Canet", "arroba@hotmail.com", "1234", true, emptyList(), null),
        User("Rodrigo", "Pineda","nach8@yahoo.com", "holamundo", true, emptyList(), null),
        User("Carolina", "Gomez","carolina.gomez@hotmail.com", "maja_2988", false, emptyList(), null)
    )

    override suspend fun getUserByEmail(email: String): Result<User?> {
        val user : User? = fakeUsers.find { user -> user.email == email }

        return Result.success(user)
    }

    override suspend fun addUser(user: User) {
        fakeUsers.add(user)
    }

    override suspend fun getAllDoctors(): Result<List<User?>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDoctor(doctorEmail: String, userEmail: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getPatientDoctorByEmail(patientEmail: String): Result<String> {
        TODO("Not yet implemented")
    }
}