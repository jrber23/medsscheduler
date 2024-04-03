package com.example.mitfg.di

import com.example.mitfg.data.medicines.MedicineDataSource
import com.example.mitfg.data.medicines.MedicineDataSourceImpl
import com.example.mitfg.data.medicines.MedicineRepository
import com.example.mitfg.data.medicines.MedicineRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MedicineBinderModule::class]
)
abstract class FakeMedicineBinderModule {

    @Singleton
    @Binds
    abstract fun bindMedicineDataSource(
        medicineDataSourceImpl: MedicineDataSourceImpl
    ) : MedicineDataSource


    @Singleton
    @Binds
    abstract fun bindMedicineRepository(
        medicineRepositoryImpl: MedicineRepositoryImpl
    ) : MedicineRepository

}