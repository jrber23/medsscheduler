/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.pharmacies

import com.example.mitfg.data.pharmacies.model.toDomain
import com.example.mitfg.domain.model.Pharmacy
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import javax.inject.Inject

class PharmacyRepositoryImpl @Inject constructor(
    private val pharmacyDataSource: PharmacyDataSource
) : PharmacyRepository {

    override suspend fun getAllPharmaciesWithinRadius(ubication: com.google.android.gms.maps.model.LatLng): Result<List<Pharmacy?>> =
        pharmacyDataSource.getAllPharmaciesWithinRadius(ubication).fold(
            onSuccess = { list ->
                val resultList = mutableListOf<Pharmacy>()

                val center = GeoLocation(ubication.latitude, ubication.longitude)
                val radius_km = 60.0 * 1000.0

                for (element in list) {
                    val docLocation = GeoLocation(element!!.lat, element.lng)
                    val distanceInKm = GeoFireUtils.getDistanceBetween(docLocation, center)
                    if (distanceInKm <= radius_km) {
                        resultList.add(element.toDomain())
                    }
                }

                Result.success(resultList)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
}