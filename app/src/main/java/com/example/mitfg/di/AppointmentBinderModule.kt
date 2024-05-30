/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.di

import com.example.mitfg.data.appointment.AppointmentDataSource
import com.example.mitfg.data.appointment.AppointmentFirestore
import com.example.mitfg.data.appointment.AppointmentRepository
import com.example.mitfg.data.appointment.AppointmentRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Binds the appointment data source and repository interfaces with its implementation
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppointmentBinderModule {

    @Binds
    abstract fun bindAppointmentDataSource(appointmentFirestore: AppointmentFirestore) : AppointmentDataSource

    @Binds
    abstract fun bindAppointmentRepository(appointmentRepositoryImpl: AppointmentRepositoryImpl) : AppointmentRepository

}