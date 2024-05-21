package com.test.employeepresence.places.data

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Tasks
import com.test.employeepresence.hours.geofence.GeofenceHelper
import com.test.employeepresence.places.domain.PlacesRepository
import com.test.employeepresence.places.domain.WorkingPlace
import com.test.employeepresence.utils.APP_LOGTAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val geocoder: Geocoder,
    private val fusedLocationProvider: FusedLocationProviderClient,
    private val geofenceHelper: GeofenceHelper,
    private val placesDataSource: PlacesDataSource
) : PlacesRepository {
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _placeFlow = MutableStateFlow<WorkingPlace?>(null)
    private val placeFlow = _placeFlow.asStateFlow()

    init {
        repositoryScope.launch {
            placesDataSource.loadPlace()?.let { place ->
                _placeFlow.emit(place)
            }
        }
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    @SuppressLint("MissingPermission")
    override suspend fun requestLocation() {
        try {
            val task =
                fusedLocationProvider.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            repositoryScope.launch {
                Tasks.await(task)?.let { location ->
                    addWorkingPlace(location.latitude, location.longitude)
                }
            }

        } catch (_: Exception) {
        }
    }

    @SuppressLint("MissingPermission")
    override fun addWorkingPlace(latitude: Double, longitude: Double) {
        repositoryScope.launch {
            val address = geocoder.getFromLocation(latitude, longitude, 1)
            val addressLine = address?.get(0)?.getAddressLine(0)
            savePlace(
                WorkingPlace(
                    latitude = latitude,
                    longitude = longitude,
                    address = addressLine
                )
            )
            Log.d(APP_LOGTAG, "addWorkingPlace $address")
        }
    }

    private suspend fun savePlace(place: WorkingPlace) {
        geofenceHelper.setupGeofence(place)
        placesDataSource.savePlace(place)
        _placeFlow.emit(place)

    }

    override fun getPlacesFlow(): Flow<WorkingPlace?> {
        return placeFlow
    }
}