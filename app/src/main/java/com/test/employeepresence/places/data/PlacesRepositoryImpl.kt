package com.test.employeepresence.places.data

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Tasks
import com.test.employeepresence.places.domain.WorkingPlace
import com.test.employeepresence.places.domain.PlacesRepository
import com.test.employeepresence.utils.APP_LOGTAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepositoryImpl @Inject constructor(
    private val geocoder: Geocoder,
    private val fusedLocationProvider: FusedLocationProviderClient,
    private val placesDataSource: PlacesDataSource
) : PlacesRepository {
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _placeFlow = MutableStateFlow<WorkingPlace?>(null)
    private val placeFlow = _placeFlow.asStateFlow()

    init {
        repositoryScope.launch {
            placesDataSource.loadPlace()?.let { place ->
                _placeFlow.emit(place)
                place.address.isNullOrEmpty().run {
                    getAddressFromLocation(place.latitude, place.longitude)
                }
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
                    val place = WorkingPlace(latitude = location.latitude, longitude = location.longitude, address = null)
                    setPlace(place)
                    getAddressFromLocation(location.latitude, location.longitude)
                }
            }

        } catch (_: Exception) {
        }
    }

    private suspend fun setPlace(place: WorkingPlace) {
        placesDataSource.savePlace(place)
        _placeFlow.emit(place)

    }

    override fun getPlacesFlow(): Flow<WorkingPlace?> {
        return placeFlow
    }

    private suspend fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val address = geocoder.getFromLocation(latitude, longitude, 1)
        address?.get(0)?.getAddressLine(0)?.let { addressLine ->
            val place = placeFlow.value
            if (place?.latitude == latitude && place.longitude == longitude) {
                setPlace(place.copy(address = addressLine))
            }
        }
        Log.d(APP_LOGTAG, "getAddressFromLocation $address")
    }
}