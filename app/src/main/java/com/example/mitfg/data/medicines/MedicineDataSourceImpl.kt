package com.example.mitfg.data.medicines

import com.example.mitfg.data.medicines.model.MedicineDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MedicineDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MedicineDataSource {
    override suspend fun getAllMedicines(): Result<List<MedicineDto?>> =
    withContext(Dispatchers.IO) {
        val docRef = firestore.collection("medicines")
        return@withContext try {
            val list = mutableListOf<MedicineDto?>()
            val snapshot = docRef.get().await()
            val documents = snapshot.documents

            for (element in documents) {
                var medicine = element.toObject<MedicineDto>()
                medicine!!.id = element.id
                list.add(medicine)
            }

            Result.success(list)
        } catch (e: FirebaseFirestoreException) {
            Result.failure(e)
        }
    }

}