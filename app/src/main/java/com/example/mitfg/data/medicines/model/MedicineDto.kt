package com.example.mitfg.data.medicines.model

data class MedicineDto(
    var name: String,
    var type: String
) {
    constructor() : this("", "")
}