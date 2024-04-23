package com.example.mitfg.data.pharmacies

import com.example.mitfg.domain.model.Pharmacy

interface PharmacyRepository {

    suspend fun getAllPharmaciesWithinRadius(ubication: com.google.android.gms.maps.model.LatLng) : Result<List<Pharmacy?>>

}