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
    override suspend fun getAllMedicines(): Result<List<Medicine?>> =
        medicineDataSource.getAllMedicines().fold(
            onSuccess = { list ->
                val resultList = mutableListOf<Medicine>()

                for (i in list.indices) {
                    resultList.add(list.get(i)!!.toDomain())
                }

                Result.success(resultList)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )

    override suspend fun addNewMedicine(medicine: Medicine) {
        medicineDataSource.addNewMedicine(medicine.toDto())
    }

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

}