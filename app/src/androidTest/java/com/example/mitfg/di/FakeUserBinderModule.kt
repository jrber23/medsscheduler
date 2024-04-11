package com.example.mitfg.di

import com.example.mitfg.data.users.UserDataSource
import com.example.mitfg.data.users.UserDataSourceImpl
import com.example.mitfg.data.users.UserRepository
import com.example.mitfg.data.users.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UserBinderModule::class]
)
abstract class FakeUserBinderModule {

    @Singleton
    @Binds
    abstract fun bindUserDataSource(
        userDataSourceImpl: UserDataSourceImpl
    ) : UserDataSource

    @Singleton
    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ) : UserRepository

}