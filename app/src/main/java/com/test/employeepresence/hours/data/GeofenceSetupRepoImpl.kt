package com.test.employeepresence.hours.data

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.test.employeepresence.hours.GeoFenceCreatorService
import com.test.employeepresence.places.domain.WorkingPlace
import com.test.employeepresence.utils.APP_LOGTAG
import com.test.employeepresence.utils.GEOFENCE_RADIUS_IN_METERS
import com.test.employeepresence.hours.domain.GeofenceSetupRepo
import com.test.employeepresence.utils.GEOFENCE_ID
import com.test.employeepresence.utils.GEOFENCE_REQUEST_CODE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class GeofenceSetupRepoImpl @Inject constructor(
    @ApplicationContext val context: Context
): GeofenceSetupRepo {
    @Inject
    lateinit var geofencingClient: GeofencingClient
    private val repositoryScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    @SuppressLint("MissingPermission")
    override fun setupGeofence(place: WorkingPlace) {
        Log.d(APP_LOGTAG, "handlePlaceUpdate $place")
        val geoFence = Geofence.Builder().
            setCircularRegion(
                place.latitude,
                place.longitude,
                GEOFENCE_RADIUS_IN_METERS
            )
            .setRequestId(GEOFENCE_ID)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_DWELL)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setLoiteringDelay(5000)
            .build()

        val geofencingRequest = GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(listOf(geoFence))
        }.build()

        repositoryScope.launch {
            geofencingClient.removeGeofences(listOf(GEOFENCE_ID)).run {
                addOnSuccessListener {
                    Log.d(APP_LOGTAG, "Geofence removed")
                }
                addOnFailureListener {
                    Log.d(APP_LOGTAG, "Geofence removing failed $it")
            }
                }
            geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
                addOnSuccessListener {
                    Log.d(APP_LOGTAG, "Geofence added")
                }
                addOnFailureListener {
                    Log.d(APP_LOGTAG, "Geofence adding failed $it")
                }
            }
        }
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeoFenceCreatorService::class.java)

        PendingIntent.getService(
            context,
            GEOFENCE_REQUEST_CODE,
            intent,
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                PendingIntent.FLAG_CANCEL_CURRENT
            } else {
                PendingIntent.FLAG_MUTABLE
            }
        )
    }
}