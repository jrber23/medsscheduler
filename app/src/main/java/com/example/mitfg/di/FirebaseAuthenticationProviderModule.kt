package com.example.mitfg.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class FirebaseAuthenticationProviderModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthentication(): FirebaseAuth =
        Firebase.auth

}