package com.example.mitfg.data.medicines

import com.example.mitfg.data.medicines.model.MedicineDto

interface MedicineDataSource {

    suspend fun getAllMedicines() : Result<List<MedicineDto?>>

    suspend fun addNewMedicine(medicine: MedicineDto)

    suspend fun getMedicineByName(name: String) : Result<MedicineDto?>

}