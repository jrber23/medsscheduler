/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.medicines

import com.example.mitfg.data.medicines.model.MedicineDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MedicineFirestore @Inject constructor(
    private val firestore: FirebaseFirestore
) : MedicineDataSource {

    /**
     * Retrieves all medicines storaged in the data source
     * @return An encapsulation of a list of medicines
     */
    override suspend fun getAllMedicines(): Result<List<MedicineDto?>> =
    withContext(Dispatchers.IO) {

        // Gets the reference to the collection where it must search
        val docRef = firestore.collection("medicines")
        return@withContext try {
            val result = mutableListOf<MedicineDto?>()

            // Awaits until all the data is collected
            val snapshot = docRef.get().await()
            val documents = snapshot.documents

            // For every retrieved document, it's mapped to a DTO, assigned its ID and added to the result list
            for (element in documents) {
                val medicine = element.toObject<MedicineDto>()
                medicine!!.id = element.id
                result.add(medicine)
            }

            Result.success(result)
        } catch (e: FirebaseFirestoreException) {
            Result.failure(e)
        }
    }

    /**
     * Adds a new medicine to the data source
     * @param medicine a DTO that contains all the data of the medicine to add.
     */
    override suspend fun addNewMedicine(medicine: MedicineDto) {
        // Gets the reference to the collection where it must search
        val docRef = firestore.collection("medicines")

        // Maps all the data in every document field
        val data = hashMapOf(
            "name" to medicine.name,
            "description" to medicine.description,
            "adverseEffects" to medicine.adverseEffects
        )

        // Awaits until the data is added
        docRef
            .add(data)
            .await()
    }

    /**
     * Retrieves a medicine with the name given
     * @param the name of the medicine to retrieve
     * @return an encapsulation of the retrieved medicine DTO.
     */
    override suspend fun getMedicineByName(name: String): Result<MedicineDto?> =
        withContext(Dispatchers.IO) {
            // Gets the reference to the collection where it must search
            val docRef = firestore.collection("medicines").whereEqualTo("name", name)
            return@withContext try {
                val snapshot = docRef.get().await()
                val document = snapshot.documents[0]

                val foundMedicine = document.toObject<MedicineDto>()

                Result.success(foundMedicine)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }

    /**
     * Checks if a medicine with the given name exists in the data source
     * @param the name of the medicine
     * @return An encapsulation of the search result.
     */
    override suspend fun existsMedicine(medicineName: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                // Gets the reference to the collection where it must search
                val docRef = firestore.collection("medicines").whereEqualTo("name", medicineName).get().await()

                // Checks if exists any document
                val result = docRef.documents.isNotEmpty()

                Result.success(result)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }

    }

}