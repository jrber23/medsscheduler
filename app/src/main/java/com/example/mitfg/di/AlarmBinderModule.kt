/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.di

import com.example.mitfg.data.instructionAlarm.AlarmDataSource
import com.example.mitfg.data.instructionAlarm.AlarmRepository
import com.example.mitfg.data.instructionAlarm.AlarmRepositoryImpl
import com.example.mitfg.data.instructionAlarm.AlarmRoom
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Binds the alarm data source and repository interfaces with its implementation
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmBinderModule {

    @Binds
    abstract fun bindAlarmDataSource(alarmRoom: AlarmRoom) : AlarmDataSource

    @Binds
    abstract fun bindAlarmRepository(alarmRepositoryImpl: AlarmRepositoryImpl) : AlarmRepository

}