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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserDataSource {
    override suspend fun getUserByEmail(email: String): Result<UserDto?> =
        withContext(Dispatchers.IO) {
            val docRef = firestore.collection("users")
            return@withContext try {
                val user = docRef.document(email).get().await()

                Result.success(user.toObject<UserDto?>())
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }


    override suspend fun addUser(user: UserDto) {
        val email = user.email

        val data = hashMapOf(
            "email" to user.email,
            "password" to user.password,
            "isDoctor" to user.isDoctor,
            "name" to user.name,
            "surnames" to user.surnames,
            "patientsList" to user.patientsList,
            "associatedDoctor" to user.associatedDoctor
        )

        firestore.collection("users").document(email)
            .set(data)
            .await()
    }

    override suspend fun getAllDoctors(): Result<List<UserDto?>> =
        withContext(Dispatchers.IO) {
            val docRef = firestore.collection("users").whereEqualTo("isDoctor", true)
            return@withContext try {
                val list = docRef.get().await().toObjects<UserDto>()

                Result.success(list)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }

    override suspend fun updateDoctor(doctorEmail: String, userEmail: String) {
        val documentRef = firestore.collection("users").document(userEmail)

        documentRef.update("associatedDoctor", doctorEmail).await()
    }

    override suspend fun getPatientDoctorByEmail(patientEmail: String): Result<String> =
        withContext(Dispatchers.IO) {
            val docRef = firestore.collection("users").document(patientEmail)
            return@withContext try {
                val doctorEmail = docRef.get().await().get("associatedDoctor").toString()

                Result.success(doctorEmail)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }




    }
}