package com.test.employeepresence.di

import com.test.employeepresence.hours.data.GeofenceSetupRepoImpl
import com.test.employeepresence.hours.domain.GeofenceSetupRepo
import com.test.employeepresence.places.data.PlacesDataSource
import com.test.employeepresence.places.data.PlacesLocalDataSourceImpl
import com.test.employeepresence.places.data.PlacesRepositoryImpl
import com.test.employeepresence.places.domain.PlacesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlacesRepoModule {
    @Binds
    @Singleton
    abstract fun bindPlacesRepository(
        repo: PlacesRepositoryImpl
    ): PlacesRepository

    @Binds
    @Singleton
    abstract fun bindGeofenceSetupRepository(
        repo: GeofenceSetupRepoImpl
    ): GeofenceSetupRepo

    @Binds
    @Singleton
    abstract fun bindPlacesLocalDataSource(
        locationDataSource: PlacesLocalDataSourceImpl
    ): PlacesDataSource
}
