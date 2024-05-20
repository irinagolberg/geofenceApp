package com.test.employeepresence.hours.domain

import com.test.employeepresence.places.domain.WorkingPlace

interface GeofenceSetupRepo {
    fun setupGeofence(place: WorkingPlace)
}