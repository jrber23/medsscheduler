/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.di

import com.example.mitfg.data.users.UserDataSource
import com.example.mitfg.data.users.UserDataSourceImpl
import com.example.mitfg.data.users.UserRepository
import com.example.mitfg.data.users.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class UserBinderModule {

    @Binds
    abstract fun bindUserDataSource(userDataSourceImpl: UserDataSourceImpl) : UserDataSource

    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl) : UserRepository


}