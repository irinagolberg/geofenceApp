package com.test.employeepresence.hours.geofence

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.test.employeepresence.places.domain.WorkingPlace
import com.test.employeepresence.utils.APP_LOGTAG
import com.test.employeepresence.utils.GEOFENCE_RADIUS_IN_METERS
import com.test.employeepresence.utils.GEOFENCE_ID
import com.test.employeepresence.utils.GEOFENCE_PLACE_EXTRA
import com.test.employeepresence.utils.GEOFENCE_REQUEST_CODE
import com.test.employeepresence.utils.LOITERING_DELAY
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeofenceHelper @Inject constructor(
    @ApplicationContext val context: Context
) {
    @Inject
    lateinit var geofencingClient: GeofencingClient
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var pendingIntent: PendingIntent? = null

    @SuppressLint("MissingPermission")
    fun setupGeofence(place: WorkingPlace) {
        Log.d(APP_LOGTAG, "handlePlaceUpdate $place")
        val geoFence = Geofence.Builder().setCircularRegion(
            place.latitude,
            place.longitude,
            GEOFENCE_RADIUS_IN_METERS
        )
            .setRequestId(GEOFENCE_ID)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setLoiteringDelay(LOITERING_DELAY)
            .build()

        val geofencingRequest = GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(listOf(geoFence))
        }.build()

        scope.launch {
            geofencingClient.removeGeofences(listOf(GEOFENCE_ID)).run {
                addOnSuccessListener {
                    Log.d(APP_LOGTAG, "Geofence removed")
                }
                addOnFailureListener {
                    Log.d(APP_LOGTAG, "Geofence removing failed $it")
                }
            }

            pendingIntent = getPendingIntent(place)
            geofencingClient.addGeofences(geofencingRequest, pendingIntent!!).run {
                addOnSuccessListener {
                    Log.d(APP_LOGTAG, "Geofence added")
                }
                addOnFailureListener {
                    Log.d(APP_LOGTAG, "Geofence adding failed $it")
                }
            }
        }
    }

    private fun getPendingIntent(place: WorkingPlace): PendingIntent {
        val intent = Intent(context, GeofenceEventsReciever::class.java)
        intent.putExtra(GEOFENCE_PLACE_EXTRA, place.address)
        return PendingIntent.getService(
            context,
            place.hashCode(),
            intent,
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                PendingIntent.FLAG_CANCEL_CURRENT
            } else {
                PendingIntent.FLAG_MUTABLE
            }
        )
    }
}