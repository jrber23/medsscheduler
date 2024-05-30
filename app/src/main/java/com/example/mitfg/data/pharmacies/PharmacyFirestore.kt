/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.pharmacies

import com.example.mitfg.data.pharmacies.model.PharmacyDto
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PharmacyFirestore @Inject constructor(
    private val firestore: FirebaseFirestore
) : PharmacyDataSource {

    /**
     * Retrieves all pharmacies located within a 60 km radius from the user's location
     * @param User's current location given in an object that contains latitude and longitude data
     * @return An encapsulation of a list of the found pharmacies.
     */
    override suspend fun getAllPharmaciesWithinRadius(ubication: LatLng): Result<List<PharmacyDto?>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                // Gets the reference to the collection where it must search
                val docRef = firestore.collection("pharmacies")

                val result = mutableListOf<PharmacyDto?>()

                // Creates de center of the circunference with user's location latitude and longitude
                val center = GeoLocation(ubication.latitude, ubication.longitude)
                val radiusKm = 60.0 * 1000.0

                // Calculates the bounds given the radius and the user's location
                val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusKm)

                // For every bound, search for any existing pharmacy, maps it to a DTO object and add it to the result list
                for (bound in bounds) {
                    val q = docRef
                        .orderBy("geohash")
                        .startAt(bound.startHash)
                        .startAt(bound.endHash)

                    val document = q.get().await().toObjects<PharmacyDto>()
                    result.addAll(document)
                }

                Result.success(result)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }
    }