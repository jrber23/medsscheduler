/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.adverseEffects

import com.example.mitfg.data.adverseEffects.model.AdverseEffectDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AdverseEffectFirestore @Inject constructor(
    private val firestore: FirebaseFirestore
) : AdverseEffectDataSource {

    /**
     * Retrieves all storage adverse effects from the data source
     * @return an encapsulation of a list of adverse effects.
     */
    override suspend fun getAllAdverseEffects(): Result<List<AdverseEffectDto?>> =
        withContext(Dispatchers.IO) {

            // Looks for the collection with the given name
            val documentRef = firestore.collection("adverse_effects")

            return@withContext try {
                val result = mutableListOf<AdverseEffectDto?>()

                // Waits until all the data is retrieved
                val snapshot = documentRef.get().await()
                val retrievedDocuments = snapshot.documents

                // For each retrieved document, it's casted to the right DTO object and assigns its ID and the DTO object is added to the result list
                for (element in retrievedDocuments) {
                    val adverseEffect = element.toObject<AdverseEffectDto?>()
                    adverseEffect!!.id = element.id
                    result.add(adverseEffect)
                }

                Result.success(result)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }
    }