package com.example.mitfg.data.medicines.model

import com.example.mitfg.domain.model.Medicine

fun MedicineDto.toDomain() : Medicine = Medicine(name = name, type = type)

fun Medicine.toDto() : MedicineDto = MedicineDto(name = name, type = type)