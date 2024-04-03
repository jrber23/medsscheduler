package com.example.mitfg.data.adverseEffects

import com.example.mitfg.data.adverseEffects.model.AdverseEffectDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AdverseEffectDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : AdverseEffectDataSource {
    override suspend fun getAllAdverseEffects(): Result<List<AdverseEffectDto?>> =
        withContext(Dispatchers.IO) {
            val docRef = firestore.collection("adverse_effects")
            return@withContext try {
                val list = mutableListOf<AdverseEffectDto?>()
                val snapshot = docRef.get().await()
                val documents = snapshot.documents

                for (element in documents) {
                    val adverseEffect = element.toObject<AdverseEffectDto?>()
                    adverseEffect!!.id = element.id
                    list.add(adverseEffect)
                }

                Result.success(list)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }
    }