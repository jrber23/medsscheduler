/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.di

import android.content.Context
import androidx.room.Room
import com.example.mitfg.data.instructionAlarm.AlarmContract.DB_NAME
import com.example.mitfg.data.instructionAlarm.AlarmDao
import com.example.mitfg.data.instructionAlarm.AlarmDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides the database object and the alarm DAO
 */
@Module
@InstallIn(SingletonComponent::class)
class AlarmProviderModule {

    @Provides
    @Singleton
    fun provideAlarmDatabase(@ApplicationContext context: Context): AlarmDatabase =
        Room.databaseBuilder(context, AlarmDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideAlarmDao(alarmDatabase: AlarmDatabase): AlarmDao =
        alarmDatabase.alarmDao()

}