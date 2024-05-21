package com.test.employeepresence.di

import android.content.Context
import androidx.room.Room
import com.test.employeepresence.places.data.db.HoursDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideInMemoryDb(@ApplicationContext context: Context): HoursDatabase {
        return Room.inMemoryDatabaseBuilder(
            context, HoursDatabase::class.java
        ).allowMainThreadQueries().build()
    }
}