package com.example.mitfg.data.medicines

import com.example.mitfg.data.medicines.model.toDomain
import com.example.mitfg.domain.model.Medicine
import javax.inject.Inject


class MedicineRepositoryImpl @Inject constructor(
    private val medicineDataSource: MedicineDataSource
) : MedicineRepository {
    override suspend fun getAllMedicines(): Result<List<Medicine?>> =
        medicineDataSource.getAllMedicines().fold(
            onSuccess = { list ->
                val resultList = mutableListOf<Medicine>()

                for (i in 0..<list.size) {
                    resultList.add(list.get(i)!!.toDomain())
                }

                Result.success(resultList)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )

}