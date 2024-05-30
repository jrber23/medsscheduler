/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.domain.model

/**
 * A pharmacy contains latitude and longitude data and a geohash to store in Firestore
 */
data class Pharmacy(
    val pharmacyName: String,
    val address: String,
    val city: String,
    val region: String,
    val lat: Double,
    val lng: Double,
    val geohash: String
)