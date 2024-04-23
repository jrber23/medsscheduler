package com.example.mitfg.data.pharmacies

import com.example.mitfg.data.pharmacies.model.toDomain
import com.example.mitfg.domain.model.Pharmacy
import javax.inject.Inject

class PharmacyRepositoryImpl @Inject constructor(
    private val pharmacyDataSource: PharmacyDataSource
) : PharmacyRepository {
    override suspend fun getAllPharmaciesWithinRadius(ubication: com.google.android.gms.maps.model.LatLng): Result<List<Pharmacy?>> =
        pharmacyDataSource.getAllPharmaciesWithinRadius(ubication).fold(
            onSuccess = { list ->
                val resultList = mutableListOf<Pharmacy>()

                for (i in list.indices) {
                    resultList.add(list.get(i)!!.toDomain())
                }

                Result.success(resultList)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
}