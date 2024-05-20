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

interface PharmacyDataSource {

    suspend fun getAllPharmaciesWithinRadius(ubication: com.google.android.gms.maps.model.LatLng) : Result<List<PharmacyDto?>>


}