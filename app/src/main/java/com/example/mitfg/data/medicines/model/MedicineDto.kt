package com.example.mitfg.data.medicines.model

data class MedicineDto(
    var id: String,
    var name: String,
    var description: String,
    var adverseEffects: List<String>
) {
    constructor() : this("", "", "", emptyList())
}