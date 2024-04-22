package com.example.mitfg.data.pharmacies

import com.example.mitfg.data.pharmacies.model.PharmacyDto

interface PharmacyDataSource {

    suspend fun getAllPharmaciesWithinRadius() : Result<PharmacyDto?>


}