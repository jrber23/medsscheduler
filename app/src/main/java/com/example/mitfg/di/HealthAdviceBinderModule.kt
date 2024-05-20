/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.di

import com.example.mitfg.data.healthAdvices.HealthAdviceDataSource
import com.example.mitfg.data.healthAdvices.HealthAdviceDataSourceImpl
import com.example.mitfg.data.healthAdvices.HealthAdviceRepository
import com.example.mitfg.data.healthAdvices.HealthAdviceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class HealthAdviceBinderModule {

    @Binds
    abstract fun bindHealthAdviceDataSource(healthAdviceDataSourceImpl: HealthAdviceDataSourceImpl): HealthAdviceDataSource

    @Binds
    abstract fun bindHealthAdviceRepository(healthAdviceRepositoryImpl: HealthAdviceRepositoryImpl): HealthAdviceRepository

}