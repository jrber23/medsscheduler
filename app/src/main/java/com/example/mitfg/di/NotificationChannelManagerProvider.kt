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
import com.example.mitfg.utils.NotificationChannelManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides the only NotificationChannelManager object instance in the application
 */
@Module
@InstallIn(SingletonComponent::class)
class NotificationChannelManagerProvider {

    @Provides
    @Singleton
    fun provideNotificationChannelManager(@ApplicationContext context: Context) : NotificationChannelManager =
        NotificationChannelManager(context)

}