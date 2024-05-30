/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.di

import com.example.mitfg.data.medicines.MedicineDataSource
import com.example.mitfg.data.medicines.MedicineFirestore
import com.example.mitfg.data.medicines.MedicineRepository
import com.example.mitfg.data.medicines.MedicineRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Binds the medicine data source and repository interfaces with its implementation
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class MedicineBinderModule {

    @Binds
    abstract fun bindMedicineDataSource(medicineFirestore: MedicineFirestore) : MedicineDataSource

    @Binds
    abstract fun bindMedicineRepository(medicineRepositoryImpl: MedicineRepositoryImpl) : MedicineRepository

}