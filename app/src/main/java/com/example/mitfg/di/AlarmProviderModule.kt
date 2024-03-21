package com.example.mitfg.di

import android.content.Context
import androidx.room.Room
import com.example.mitfg.data.instructionAlarm.AlarmContact.DB_NAME
import com.example.mitfg.data.instructionAlarm.AlarmDao
import com.example.mitfg.data.instructionAlarm.AlarmDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AlarmProviderModule {

    @Provides
    @Singleton
    fun provideAlarmDatabase(@ApplicationContext context: Context): AlarmDatabase =
        Room.databaseBuilder(context, AlarmDatabase::class.java, DB_NAME).build()

    @Provides
    fun provideAlarmDao(alarmDatabase: AlarmDatabase): AlarmDao =
        alarmDatabase.alarmDao()

}