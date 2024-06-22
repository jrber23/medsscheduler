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

class UserFirestore @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserDataSource {

    /**
     * Retrives a user with the email address given
     * @param email The email address
     * @return An encapsulation of a UserDTO object that contains all the data
     */
    override suspend fun getUserByEmail(email: String): Result<UserDto?> =
        withContext(Dispatchers.IO) {

            // Gets the reference to the collection where it must search
            val docRef = firestore.collection("users")
            return@withContext try {
                // Awaits until the data is retrieved
                val user = docRef.document(email).get().await()
                val result = user.toObject<UserDto?>()

                Result.success(result)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }

    /**
     * Adds a new user to the data source
     * @param user A UserDto object that contains all the data
     */
    override suspend fun addUser(user: UserDto) {
        val email = user.email

        // Maps every DTO data to its document field
        val data = hashMapOf(
            "email" to user.email,
            "password" to user.password,
            "isDoctor" to user.isDoctor,
            "name" to user.name,
            "surnames" to user.surnames,
            "patientsList" to user.patientsList,
            "associatedDoctor" to user.associatedDoctor,
            "drugInteractions" to user.drugInteractions
        )

        // Awaits until the data has been added
        firestore.collection("users").document(email)
            .set(data)
            .await()
    }

    /**
     * Retrieves all doctors registered in the data source
     * @return An encapsulation of a list with all retrieved UsersDTO which are doctors.
     */
    override suspend fun getAllDoctors(): Result<List<UserDto?>> =
        withContext(Dispatchers.IO) {

            // Gets the reference to the collection where it must search
            val docRef = firestore.collection("users").whereEqualTo("isDoctor", true)
            return@withContext try {
                // Awaits until the data is retrieved. Then, it's mapped to DTO object
                val result = docRef.get().await().toObjects<UserDto>()

                Result.success(result)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }

    /**
     * Assigns a doctor to a determinated patient
     * @param doctorEmail The doctor email address
     * @param userEmail The patient email address
     */
    override suspend fun updateDoctor(doctorEmail: String, userEmail: String) {
        // Gets the reference to the collection where it must search
        val documentRef = firestore.collection("users").document(userEmail)

        // Awaits until the data is updated
        documentRef.update("associatedDoctor", doctorEmail).await()
    }

    /**
     * Returns the associated doctor's email address of a determinated user
     * @param patientEmail The patient email address
     * @return An encapsulation of the found email address
     */
    override suspend fun getPatientDoctorByEmail(patientEmail: String): Result<String> =
        withContext(Dispatchers.IO) {

            // Gets the reference to the collection where it must search
            val docRef = firestore.collection("users").document(patientEmail)
            return@withContext try {
                // Awaits until the doctor email address has been retrieved
                val doctorEmail = docRef.get().await().get("associatedDoctor").toString()

                Result.success(doctorEmail)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
    }

    /**
     * Retrieves all drug interactions of a user
     * @param patientEmail The patient email address
     * @return An encapsulation of a list of every user's drug interaction
     */
    override suspend fun getDrugInteractionsByEmail(patientEmail: String): Result<List<String>> =
        withContext(Dispatchers.IO) {
            // Gets the reference to the collection where it must search
            val docRef = firestore.collection("users").document(patientEmail)
            return@withContext try {

                // Awaits until all the data is retrieved
                val list = docRef.get().await().get("drugInteractions") as ArrayList<String>
                val resultList = mutableListOf<String>()

                // For each drug interaction, it retrieves the name of the right adverse effect and then its added to the result list
                for (element in list) {
                    val drugInteraction = firestore.collection("adverse_effects").document(element).get().await().get("name") as String

                    resultList.add(drugInteraction)
                }

                Result.success(resultList)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }

}