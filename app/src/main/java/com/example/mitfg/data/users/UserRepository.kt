/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.users

import com.example.mitfg.domain.model.User

interface UserRepository {

    /**
     * Retrives a user with the email address given
     * @param email The email address
     * @return An encapsulation of a User domain model object that contains all the data
     */
    suspend fun getUserByEmail(email: String) : Result<User?>

    /**
     * Adds a new user to the data source
     * @param user A User domain model object that contains all the data
     */
    suspend fun addUser(user: User)

    /**
     * Retrieves all doctors registered in the data source
     * @return An encapsulation of a list with all retrieved User domain model objects which are doctors.
     */
    suspend fun getAllDoctors() : Result<List<User?>>

    /**
     * Assigns a doctor to a determinated patient
     * @param doctorEmail The doctor email address
     * @param userEmail The patient email address
     */
    suspend fun updateDoctor(doctorEmail: String, userEmail: String)

    /**
     * Returns the associated doctor's email address of a determinated user
     * @param patientEmail The patient email address
     * @return An encapsulation of the found email address
     */
    suspend fun getPatientDoctorByEmail(patientEmail: String) : Result<String>

    /**
     * Retrieves all drug interactions of a user
     * @param patientEmail The patient email address
     * @return An encapsulation of a list of every user's drug interaction
     */
    suspend fun getDrugInteractionsByEmail(patientEmail: String) : Result<List<String>>

}