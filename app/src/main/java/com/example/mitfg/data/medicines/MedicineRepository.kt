/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.medicines

import com.example.mitfg.domain.model.Medicine

interface MedicineRepository {

    suspend fun getAllMedicines(): Result<List<Medicine?>>

    suspend fun addNewMedicine(medicine: Medicine)

    suspend fun getMedicineByName(name: String) : Result<Medicine?>

}