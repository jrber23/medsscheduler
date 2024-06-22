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
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MedicineBinderModule::class]
)
abstract class FakeMedicineBinderModule {

    @Singleton
    @Binds
    abstract fun bindMedicineDataSource(
        medicineDataSourceImpl: MedicineFirestore
    ) : MedicineDataSource


    @Singleton
    @Binds
    abstract fun bindMedicineRepository(
        medicineRepositoryImpl: MedicineRepositoryImpl
    ) : MedicineRepository

}