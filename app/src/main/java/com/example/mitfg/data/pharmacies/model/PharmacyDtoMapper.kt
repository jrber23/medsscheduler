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

fun PharmacyDto.toDomain() : Pharmacy = Pharmacy(pharmacyName = pharmacyName, address = address, city = city, region = region, lat = lat, lng = lng, geohash = geohash)

fun PharmacyDto.toDto() : PharmacyDto = PharmacyDto(pharmacyName = pharmacyName, address = address, city = city, region = region, lat = lat, lng = lng, geohash = geohash)