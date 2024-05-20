/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.di

import com.example.mitfg.data.FakeAlarmRepository
import com.example.mitfg.data.instructionAlarm.AlarmRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AlarmBinderModule::class]
)
abstract class FakeAlarmBinderModule {

    @Singleton
    @Binds
    abstract fun bindAlarmRepository(
        alarmRepositoryImpl: FakeAlarmRepository
    ) : AlarmRepository

}