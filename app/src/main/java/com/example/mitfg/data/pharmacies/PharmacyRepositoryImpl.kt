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
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class PharmacyRepositoryImpl @Inject constructor(
    private val pharmacyDataSource: PharmacyDataSource
) : PharmacyRepository {

    /**
     * Retrieves all pharmacies located within a 60 km radius from the user's location
     * @param User's current location given in an object that contains latitude and longitude data
     * @return An encapsulation of a list of the found pharmacies. It's mapped to a domain model object
     */
    override suspend fun getAllPharmaciesWithinRadius(ubication: LatLng): Result<List<Pharmacy?>> =
        pharmacyDataSource.getAllPharmaciesWithinRadius(ubication).fold(
            onSuccess = { foundPharmacies ->
                val resultList = mutableListOf<Pharmacy>()

                // Creates de center of the circunference with user's location latitude and longitude
                val center = GeoLocation(ubication.latitude, ubication.longitude)
                val radius_km = 60.0 * 1000.0

                /* For every found pharmacy, checks its distance from the user location.
                   If it's inside the boundaries, it's added to the result list. */
                for (pharmacy in foundPharmacies) {
                    val docLocation = GeoLocation(pharmacy!!.lat, pharmacy.lng)
                    val distanceInKm = GeoFireUtils.getDistanceBetween(docLocation, center)
                    if (distanceInKm <= radius_km) {
                        resultList.add(pharmacy.toDomain())
                    }
                }

                Result.success(resultList)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
}