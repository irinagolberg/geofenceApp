package com.test.employeepresence.di

import android.content.Context
import androidx.room.Room
import com.test.employeepresence.hours.domain.HoursInteractor
import com.test.employeepresence.hours.domain.HoursRepository
import com.test.employeepresence.places.data.db.HoursDao
import com.test.employeepresence.places.data.db.HoursDatabase
import com.test.employeepresence.places.domain.PlacesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HoursInstancesModule {

    @Provides
    @Singleton
    fun provideHoursDao(@ApplicationContext applicationContext: Context): HoursDao {
        return Room.databaseBuilder(
            applicationContext,
            HoursDatabase::class.java,
            "location_hours.db"
        ).build().hoursDao()
    }


    @Provides
    fun provideHoursInteractor(
        placesRepo: PlacesRepository,
        hoursRepo: HoursRepository
    ): HoursInteractor {
        return HoursInteractor(placesRepository = placesRepo, hoursRepository = hoursRepo)
    }
}
