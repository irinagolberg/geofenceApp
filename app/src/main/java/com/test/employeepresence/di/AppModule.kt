package com.test.employeepresence.di

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.test.employeepresence.places.domain.PlacesInteractor
import com.test.employeepresence.places.domain.PlacesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGeocoder(@ApplicationContext applicationContext: Context): Geocoder {
        return Geocoder(applicationContext, Locale.getDefault())
    }

    @Provides
    @Singleton
    fun provideFusedLocationProvider(@ApplicationContext applicationContext: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    @Provides
    fun provideGeoFenceClient(
        @ApplicationContext applicationContext: Context
    ): GeofencingClient {
        return LocationServices.getGeofencingClient(applicationContext)
    }

    @Provides
    fun providePlacesInteractor(
        placesRepo: PlacesRepository
    ): PlacesInteractor {
        return PlacesInteractor(placesRepo)
    }
}
