package com.example.mitfg.di

import com.example.mitfg.data.appointment.AppointmentDataSource
import com.example.mitfg.data.appointment.AppointmentDataSourceImpl
import com.example.mitfg.data.appointment.AppointmentRepository
import com.example.mitfg.data.appointment.AppointmentRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class AppointmentBinderModule {

    @Binds
    abstract fun bindAppointmentDataSource(appointmentDataSourceImpl: AppointmentDataSourceImpl) : AppointmentDataSource

    @Binds
    abstract fun bindAppointmentRepository(appointmentRepositoryImpl: AppointmentRepositoryImpl) : AppointmentRepository

}