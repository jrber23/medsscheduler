/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.users

import com.example.mitfg.data.users.model.UserDto

interface UserDataSource {

    suspend fun getUserByEmail(email: String) : Result<UserDto?>

    suspend fun addUser(user: UserDto)

    suspend fun getAllDoctors() : Result<List<UserDto?>>

    suspend fun updateDoctor(doctorEmail: String, userEmail: String)

    suspend fun getPatientDoctorByEmail(patientEmail: String) : Result<String>

    suspend fun getDrugInteractionsByEmail(patientEmail: String) : Result<List<String>>

}