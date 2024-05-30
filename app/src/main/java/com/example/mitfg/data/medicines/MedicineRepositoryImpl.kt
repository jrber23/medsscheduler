/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.data.medicines

import com.example.mitfg.data.medicines.model.toDomain
import com.example.mitfg.data.medicines.model.toDto
import com.example.mitfg.domain.model.Medicine
import javax.inject.Inject


class MedicineRepositoryImpl @Inject constructor(
    private val medicineDataSource: MedicineDataSource
) : MedicineRepository {

    /**
     * Retrieves all medicines storaged in the data source
     * @return An encapsulation of a list of medicines. It's mapped to a domain model object
     */
    override suspend fun getAllMedicines(): Result<List<Medicine?>> =
        medicineDataSource.getAllMedicines().fold(
            onSuccess = { list ->
                val resultList = mutableListOf<Medicine>()

                for (i in list.indices) {
                    resultList.add(list[i]!!.toDomain())
                }

                Result.success(resultList)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )

    /**
     * Adds a new medicine to the data source
     * @param medicine a domain model object that contains all the data of the medicine to add.
     */
    override suspend fun addNewMedicine(medicine: Medicine) {
        medicineDataSource.addNewMedicine(medicine.toDto())
    }

    /**
     * Retrieves a medicine with the name given
     * @param the name of the medicine to retrieve
     * @return an encapsulation of the retrieved medicine domain model object.
     */
    override suspend fun getMedicineByName(name: String): Result<Medicine?> =
        medicineDataSource.getMedicineByName(name).fold(
            onSuccess = { foundMedicine ->
                val result = foundMedicine?.toDomain()

                Result.success(result)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )

    /**
     * Checks if a medicine with the given name exists in the data source
     * @param the name of the medicine
     * @return An encapsulation of the search result.
     */
    override suspend fun existsMedicine(medicineName: String): Result<Boolean> =
        medicineDataSource.existsMedicine(medicineName).fold(
            onSuccess = { itExists ->
                Result.success(itExists)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
}
