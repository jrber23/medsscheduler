/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.users

import com.example.mitfg.data.users.model.toDomain
import com.example.mitfg.data.users.model.toDto
import com.example.mitfg.domain.model.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {

    /**
     * Retrives a user with the email address given
     * @param email The email address
     * @return An encapsulation of a User domain model object that contains all the data
     */
    override suspend fun getUserByEmail(email: String): Result<User?> =
        userDataSource.getUserByEmail(email).fold(
            onSuccess = { foundUser ->
                Result.success(foundUser?.toDomain())
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )

    /**
     * Adds a new user to the data source
     * @param user A User domain model object that contains all the data
     */
    override suspend fun addUser(user: User) {
        userDataSource.addUser(user.toDto())
    }

    /**
     * Retrieves all doctors registered in the data source
     * @return An encapsulation of a list with all retrieved User domain model objects which are doctors.
     */
    override suspend fun getAllDoctors(): Result<List<User?>> =
        userDataSource.getAllDoctors().fold(
            onSuccess = { list ->
                val resultList = mutableListOf<User?>()
                for (element in list) {
                    resultList.add(element?.toDomain())
                }

                Result.success(resultList)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )

    /**
     * Assigns a doctor to a determinated patient
     * @param doctorEmail The doctor email address
     * @param userEmail The patient email address
     */
    override suspend fun updateDoctor(doctorEmail: String, userEmail: String) {
        userDataSource.updateDoctor(doctorEmail, userEmail)
    }

    /**
     * Returns the associated doctor's email address of a determinated user
     * @param patientEmail The patient email address
     * @return An encapsulation of the found email address
     */
    override suspend fun getPatientDoctorByEmail(patientEmail: String): Result<String> =
        userDataSource.getPatientDoctorByEmail(patientEmail).fold(
            onSuccess = { result ->
                Result.success(result)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )

    /**
     * Retrieves all drug interactions of a user
     * @param patientEmail The patient email address
     * @return An encapsulation of a list of every user's drug interaction
     */
    override suspend fun getDrugInteractionsByEmail(patientEmail: String): Result<List<String>> =
        userDataSource.getDrugInteractionsByEmail(patientEmail).fold(
            onSuccess = { result ->
                Result.success(result)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )

}