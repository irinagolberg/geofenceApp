package com.test.employeepresence.places.domain

import kotlinx.coroutines.flow.Flow

interface PlacesRepository {
    fun getPlacesFlow(): Flow<WorkingPlace?>
    suspend fun requestLocation()
}