package com.example.mitfg.data.pharmacies

import com.example.mitfg.data.pharmacies.model.PharmacyDto

interface PharmacyDataSource {

    suspend fun getAllPharmaciesWithinRadius(ubication: com.google.android.gms.maps.model.LatLng) : Result<List<PharmacyDto?>>


}