/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.di

import com.example.mitfg.utils.FirebaseTranslator
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Provides the only FirebaseTranslator object instance in the application
 */
@Module
@InstallIn(SingletonComponent::class)
class FirebaseTranslatorProvider {

    fun provideFirebaseTranslator() : FirebaseTranslator =
        FirebaseTranslator()

}