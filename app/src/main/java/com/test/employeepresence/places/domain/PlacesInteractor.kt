package com.test.employeepresence.places.domain

import javax.inject.Inject

class PlacesInteractor @Inject constructor(private val placesRepo: PlacesRepository) {
    val placesFlow = placesRepo.getPlacesFlow()
    suspend fun requestCurrentLocation() {
        placesRepo.requestLocation()
    }

    fun addWorkingPlace(latitude: Double, longitude: Double) {
        placesRepo.addWorkingPlace(latitude, longitude)

    }
}