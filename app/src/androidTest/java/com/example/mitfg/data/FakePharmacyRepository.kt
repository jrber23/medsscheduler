/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data

import com.example.mitfg.data.pharmacies.PharmacyRepository
import com.example.mitfg.domain.model.Pharmacy
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class FakePharmacyRepository @Inject constructor() : PharmacyRepository {

    private val fakePharmacies = mutableListOf<Pharmacy>(
        Pharmacy("Pharmacy 1", "c/ Example 1", "Valencia", "Valencia", 2.0, 0.0, "abcdefg"),
        Pharmacy("Pharmacy 2", "c/ Example 2", "Xirivella", "Valencia", 50.0, 6.0, "hijk"),
        Pharmacy("Pharmacy 3", "c/ Example 3", "Alaquàs", "Valencia", 75.0, 11.0, "lmno"),
        Pharmacy("Pharmacy 4", "c/ Example 4", "Manises", "Valencia", 80.0, 48.0, "pqrst")
    )

    override suspend fun getAllPharmaciesWithinRadius(ubication: LatLng): Result<List<Pharmacy?>> {
        return Result.success(fakePharmacies)
    }


}