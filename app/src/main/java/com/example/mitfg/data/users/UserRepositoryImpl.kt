package com.example.mitfg.data.users

import com.example.mitfg.data.users.model.toDomain
import com.example.mitfg.data.users.model.toDto
import com.example.mitfg.domain.model.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun getUserByEmail(email: String): Result<User?> =
        userDataSource.getUserByEmail(email).fold(
            onSuccess = { user ->
                Result.success(user?.toDomain())
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )

    override suspend fun addUser(user: User) {
        userDataSource.addUser(user.toDto())
    }

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

    override suspend fun updateDoctor(doctorEmail: String, userEmail: String) {
        userDataSource.updateDoctor(doctorEmail, userEmail)
    }

    override suspend fun getPatientDoctorByEmail(patientEmail: String): Result<String> =
        userDataSource.getPatientDoctorByEmail(patientEmail).fold(
            onSuccess = { result ->
                Result.success(result)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
    }