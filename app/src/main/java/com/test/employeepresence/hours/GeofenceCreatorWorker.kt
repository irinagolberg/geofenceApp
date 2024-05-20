package com.test.employeepresence.hours

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.test.employeepresence.places.domain.PlacesInteractor
import com.test.employeepresence.places.domain.WorkingPlace
import com.test.employeepresence.utils.APP_LOGTAG
import com.test.employeepresence.utils.GEOFENCE_RADIUS_IN_METERS
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class GeofenceCreatorWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    //private val geofencingClient: GeofencingClient
    private val interactor: PlacesInteractor

) : CoroutineWorker(context, workerParams) {

    //@Inject
    //lateinit var geofencingClient: GeofencingClient
    override suspend fun doWork(): Result {
        Log.d(APP_LOGTAG, "doWork")

        observePlacesFlow()
        return Result.success()
    }

    private suspend fun observePlacesFlow() {
        Log.d(APP_LOGTAG, "Ira observePlacesFlow")
        /*interactor.placesFlow.collect { place ->
            place?.let {
                handlePlaceUpdate(it)
            }
        }*/
    }

    @SuppressLint("MissingPermission")
    private fun handlePlaceUpdate(place: WorkingPlace) {
        Log.d(APP_LOGTAG, "handlePlaceUpdate $place")
        val geoFence = Geofence.Builder()
            .setRequestId("GEOFENCE_ID")
            .setCircularRegion(
                place.latitude,
                place.longitude,
                GEOFENCE_RADIUS_IN_METERS
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val geofencingRequest = GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(listOf(geoFence))
        }.build()

        /*geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
            addOnSuccessListener {
                Log.d(APP_LOGTAG, "Geofence added")
            }
            addOnFailureListener {
                Log.d(APP_LOGTAG, "Geofence adding failed $it")
            }
        }*/
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(applicationContext, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}