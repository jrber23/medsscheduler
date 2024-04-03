package com.example.mitfg.data

import com.example.mitfg.data.medicines.MedicineRepository
import com.example.mitfg.domain.model.Medicine
import javax.inject.Inject


class FakeMedicineRepository @Inject constructor() : MedicineRepository {

    private val fakeMedicines = mutableListOf<Medicine>(
        Medicine("0", "Biotina", "Para el cabello", emptyList()),
        Medicine("0", "Paracetamol", "Para el dolor de cabeza", emptyList()),
        Medicine("0", "Aspirina", "Para infartos", emptyList()),
        Medicine("0", "Calcio", "Para los huesos", listOf("Náuseas", "Temblores"))
    )
    override suspend fun getAllMedicines(): Result<List<Medicine?>> {
        return Result.success(fakeMedicines)
    }

    override suspend fun addNewMedicine(medicine: Medicine) {
        fakeMedicines.add(medicine)
    }
}