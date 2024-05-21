package com.test.employeepresence.places.data

import com.test.employeepresence.places.domain.WorkingPlace

interface PlacesDataSource {
    suspend fun savePlace(place: WorkingPlace)
    suspend fun loadPlace(): WorkingPlace?
}

