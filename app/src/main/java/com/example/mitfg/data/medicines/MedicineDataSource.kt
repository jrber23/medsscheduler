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

    /**
     * Retrieves all medicines storaged in the data source
     * @return An encapsulation of a list of medicines
     */
    suspend fun getAllMedicines() : Result<List<MedicineDto?>>

    /**
     * Adds a new medicine to the data source
     * @param medicine a DTO that contains all the data of the medicine to add.
     */
    suspend fun addNewMedicine(medicine: MedicineDto)

    /**
     * Retrieves a medicine with the name given
     * @param the name of the medicine to retrieve
     * @return an encapsulation of the retrieved medicine DTO.
     */
    suspend fun getMedicineByName(name: String) : Result<MedicineDto?>

    /**
     * Checks if a medicine with the given name exists in the data source
     * @param the name of the medicine
     * @return An encapsulation of the search result.
     */
    suspend fun existsMedicine(medicineName: String) : Result<Boolean>

}