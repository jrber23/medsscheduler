package com.example.mitfg.data.pharmacies.model

import com.example.mitfg.domain.model.Pharmacy

fun PharmacyDto.toDomain() : Pharmacy = Pharmacy(pharmacyName = pharmacyName, address = address, city = city, region = region)

fun PharmacyDto.toDto() : PharmacyDto = PharmacyDto(pharmacyName = pharmacyName, address = address, city = city, region = region)