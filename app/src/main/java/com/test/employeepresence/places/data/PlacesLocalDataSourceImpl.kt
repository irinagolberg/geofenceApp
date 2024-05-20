package com.test.employeepresence.places.data

import com.test.employeepresence.places.data.db.PlacesDao
import com.test.employeepresence.places.data.db.PlacesEntity
import com.test.employeepresence.places.domain.WorkingPlace
import javax.inject.Inject

class PlacesLocalDataSourceImpl @Inject constructor(private val placesDao: PlacesDao):
    PlacesDataSource {
    override suspend fun savePlace(place: WorkingPlace): Long {
        return placesDao.saveLocation(
            PlacesEntity(
                latitude = place.latitude,
                longitude = place.longitude,
                address = place.address
            )
        )
    }

    override suspend fun loadPlace(): WorkingPlace? {
        return placesDao.getPlaces().firstOrNull()?.let { place ->
            WorkingPlace(
                cacheId = place.id,
                latitude = place.latitude,
                longitude = place.longitude,
                address = place.address
            )
        }
    }
}