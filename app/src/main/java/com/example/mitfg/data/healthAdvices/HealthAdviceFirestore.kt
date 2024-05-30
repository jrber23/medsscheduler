/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.healthAdvices

import com.example.mitfg.data.healthAdvices.model.HealthAdviceDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HealthAdviceFirestore @Inject constructor(
    private val firestore: FirebaseFirestore
) : HealthAdviceDataSource {

    /**
     * Retrieves all the health advices storage in the data source
     * @return an encapsulation of a list of health advices
     */
    override suspend fun getAllHealthAdvices(): Result<List<HealthAdviceDto?>> =
        withContext(Dispatchers.IO) {

            // Gets a reference to the collection where it must search
            val docRef = firestore.collection("health_advices")
            return@withContext try {
                val result = mutableListOf<HealthAdviceDto?>()

                // It awaits until all the data is collected
                val snapshot = docRef.get().await()
                val documents = snapshot.documents

                // For every retrieved document, it's mapped to the right DTO object, assigned its ID and added to the result list
                for (element in documents) {
                    val healthAdvice = element.toObject<HealthAdviceDto?>()
                    healthAdvice!!.id = element.id
                    result.add(element.toObject<HealthAdviceDto?>())
                }

                Result.success(result)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }
    }