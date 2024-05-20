package com.test.employeepresence.di

import com.test.employeepresence.hours.data.HoursDataSource
import com.test.employeepresence.hours.data.HoursLocalDataSourceImpl
import com.test.employeepresence.hours.data.HoursRepositoryImpl
import com.test.employeepresence.hours.domain.HoursRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HoursRepoModule {
    @Binds
    @Singleton
    abstract fun bindHoursRepository(
        repo: HoursRepositoryImpl
    ): HoursRepository

    @Binds
    @Singleton
    abstract fun bindHoursLocalDataSource(
        dataSource: HoursLocalDataSourceImpl
    ): HoursDataSource
}
