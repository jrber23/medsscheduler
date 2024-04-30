package com.example.mitfg.data.medicines

import com.example.mitfg.domain.model.Medicine

interface MedicineRepository {

    suspend fun getAllMedicines(): Result<List<Medicine?>>

    suspend fun addNewMedicine(medicine: Medicine)

    suspend fun getMedicineByName(name: String) : Result<Medicine?>

}