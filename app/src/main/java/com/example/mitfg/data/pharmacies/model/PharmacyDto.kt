package com.example.mitfg.data.pharmacies.model

data class PharmacyDto(
    val pharmacyName: String,
    val address: String,
    val city: String,
    val region: String,
    val lat: Double,
    val lng: Double,
    val geohash: String
) {
    constructor() : this("", "", "", "", 0.0, 0.0, "")
}
