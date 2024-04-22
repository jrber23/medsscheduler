package com.example.mitfg.data.pharmacies

import com.example.mitfg.data.pharmacies.model.PharmacyDto
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class PharmacyDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PharmacyDataSource {
    override suspend fun getAllPharmaciesWithinRadius(): Result<PharmacyDto?> {

        val docRef = firestore.collection("pharmacies")

        return Result.failure(Exception("algo salió mal"))
    }
}