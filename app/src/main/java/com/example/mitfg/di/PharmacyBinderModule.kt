/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.di

import com.example.mitfg.data.pharmacies.PharmacyDataSource
import com.example.mitfg.data.pharmacies.PharmacyDataSourceImpl
import com.example.mitfg.data.pharmacies.PharmacyRepository
import com.example.mitfg.data.pharmacies.PharmacyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class PharmacyBinderModule {

    @Binds
    abstract fun bindPharmacyDataSource(pharmacyDataSourceImpl: PharmacyDataSourceImpl) : PharmacyDataSource

    @Binds
    abstract fun bindPharmacyRepository(pharmacyRepositoryImpl: PharmacyRepositoryImpl) : PharmacyRepository

}