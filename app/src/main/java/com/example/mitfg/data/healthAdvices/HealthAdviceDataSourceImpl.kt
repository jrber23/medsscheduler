package com.example.mitfg.data.healthAdvices

import com.example.mitfg.data.healthAdvices.model.HealthAdviceDto
import com.example.mitfg.data.medicines.model.MedicineDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HealthAdviceDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : HealthAdviceDataSource {
    override suspend fun getAllHealthAdvices(): Result<List<HealthAdviceDto?>> =
        withContext(Dispatchers.IO) {
            val docRef = firestore.collection("health_advices")
            return@withContext try {
                val list = mutableListOf<HealthAdviceDto?>()
                val snapshot = docRef.get().await()
                val documents = snapshot.documents

                for (element in documents) {
                    var healthAdvice = element.toObject<MedicineDto>()
                    healthAdvice!!.id = element.id
                    list.add(element.toObject<HealthAdviceDto?>())
                }

                Result.success(list)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }
    }