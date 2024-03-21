package com.example.mitfg.data.medicines.model

data class MedicineDto(
    var id: String,
    var name: String,
    var type: String
) {
    constructor() : this("", "", "")
}