package com.test.employeepresence.places.domain

import com.test.employeepresence.hours.domain.GeofenceSetupRepo
import javax.inject.Inject

class PlacesInteractor @Inject constructor(private val placesRepo: PlacesRepository, private val geoFenceSetupRepo: GeofenceSetupRepo) {
    val placesFlow = placesRepo.getPlacesFlow()
    suspend fun requestCurrentLocation() {
        placesRepo.requestLocation()
    }

    fun setupGeofence(place: WorkingPlace) {
        geoFenceSetupRepo.setupGeofence(place)
    }
}