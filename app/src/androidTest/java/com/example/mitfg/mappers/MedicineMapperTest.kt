/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.mappers

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mitfg.data.medicines.model.MedicineDto
import com.example.mitfg.data.medicines.model.toDomain
import com.example.mitfg.data.medicines.model.toDto
import com.example.mitfg.domain.model.Medicine
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MedicineMapperTest {

    @Test
    fun medicineDtoToDomain_Test() {
        val medicineDto = MedicineDto("0", "Paracetamol", "Pastilla", emptyList())

        TestCase.assertTrue(medicineDto.toDomain() is Medicine)
    }

    @Test
    fun medicineToDto_Test() {
        val medicine = Medicine("0", "Paracetamol", "Pastilla", emptyList())

        TestCase.assertTrue(medicine.toDto() is MedicineDto)
    }

}