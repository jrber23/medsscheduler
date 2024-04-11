package com.example.mitfg.di

import com.example.mitfg.firebase.FirebaseTranslator
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HealthAdviceProviderModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore() : FirebaseFirestore =
        Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseTranslator() : FirebaseTranslator =
        FirebaseTranslator()

}