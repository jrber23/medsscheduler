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
                val medicine = element.toObject<MedicineDto>()
                medicine!!.id = element.id
                list.add(medicine)
            }

            Result.success(list)
        } catch (e: FirebaseFirestoreException) {
            Result.failure(e)
        }
    }

    override suspend fun addNewMedicine(medicine: MedicineDto) {
        val docRef = firestore.collection("medicines")
        val data = hashMapOf(
            "name" to medicine.name,
            "description" to medicine.description,
            "adverseEffects" to medicine.adverseEffects
        )

        docRef
            .add(data)
            .await()
    }

    override suspend fun getMedicineByName(name: String): Result<MedicineDto?> =
        withContext(Dispatchers.IO) {
            val docRef = firestore.collection("medicines").whereEqualTo("name", name)
            return@withContext try {
                val snapshot = docRef.get().await()
                val document = snapshot.documents.get(0)

                val foundMedicine = document.toObject<MedicineDto>()

                Result.success(foundMedicine)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }

}