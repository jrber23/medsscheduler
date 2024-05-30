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
import com.google.android.gms.maps.model.LatLng

interface PharmacyDataSource {

    /**
     * Retrieves all pharmacies located within a 60 km radius from the user's location
     * @param User's current location given in an object that contains latitude and longitude data
     * @return An encapsulation of a list of the found pharmacies.
     */
    suspend fun getAllPharmaciesWithinRadius(ubication: LatLng) : Result<List<PharmacyDto?>>


}