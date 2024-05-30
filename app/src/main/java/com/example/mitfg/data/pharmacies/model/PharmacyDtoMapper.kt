/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.pharmacies.model

import com.example.mitfg.domain.model.Pharmacy

/**
 * It converts an object PharmacyDto into the domain model object
 */
fun PharmacyDto.toDomain() : Pharmacy =
    Pharmacy(
        pharmacyName = pharmacyName,
        address = address,
        city = city,
        region = region,
        lat = lat,
        lng = lng,
        geohash = geohash
    )

/**
 * It converts a domain model object into an PharmacyDto object
 */
fun PharmacyDto.toDto() : PharmacyDto =
    PharmacyDto(
        pharmacyName = pharmacyName,
        address = address,
        city = city,
        region = region,
        lat = lat,
        lng = lng,
        geohash = geohash
    )