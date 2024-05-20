/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.medicines

import com.example.mitfg.data.medicines.model.MedicineDto

interface MedicineDataSource {

    suspend fun getAllMedicines() : Result<List<MedicineDto?>>

    suspend fun addNewMedicine(medicine: MedicineDto)

    suspend fun getMedicineByName(name: String) : Result<MedicineDto?>

}