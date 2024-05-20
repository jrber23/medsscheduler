/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.di

import com.example.mitfg.data.adverseEffects.AdverseEffectDataSource
import com.example.mitfg.data.adverseEffects.AdverseEffectDataSourceImpl
import com.example.mitfg.data.adverseEffects.AdverseEffectRepository
import com.example.mitfg.data.adverseEffects.AdverseEffectRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class AdverseEffectBinderModule {

    @Binds
    abstract fun bindAdverseEffectDataSource(adverseEffectDataSourceImpl: AdverseEffectDataSourceImpl) : AdverseEffectDataSource

    @Binds
    abstract fun bindAdverseEffectRepository(adverseEffectRepositoryImpl: AdverseEffectRepositoryImpl) : AdverseEffectRepository

}