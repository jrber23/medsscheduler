package com.example.mitfg.data.pharmacies.model

data class PharmacyDto(
    val pharmacyName: String,
    val address: String,
    val city: String,
    val region: String
) {
    constructor() : this("", "", "", "")
}
