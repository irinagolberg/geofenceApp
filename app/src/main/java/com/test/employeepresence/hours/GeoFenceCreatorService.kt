package com.test.employeepresence.hours

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.test.employeepresence.hours.domain.HoursInteractor
import com.test.employeepresence.utils.APP_LOGTAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GeoFenceCreatorService: LifecycleService() {
    companion object {
        const val EXTRA_STOP_SERVICE = "stop"
    }

    @Inject lateinit var interactor: HoursInteractor
    /*private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }*/

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val result = super.onStartCommand(intent, flags, startId)
        Log.d(APP_LOGTAG, "Geofence service intent: $intent")
        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(intent) }
        if (geofencingEvent == null || geofencingEvent.hasError()) {
            geofencingEvent?.errorCode?.let {
                GeofenceStatusCodes
                    .getStatusCodeString(it)
                Log.e(APP_LOGTAG, "Geofence event error: $it")
            }
            return result
        }
        Log.d(APP_LOGTAG, "Geofence receiver $geofencingEvent")
        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT
        ) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            Log.i(APP_LOGTAG, "Geofence triggered: $triggeringGeofences")
            val latitude = geofencingEvent.triggeringLocation?.latitude
            val longitude = geofencingEvent.triggeringLocation?.longitude
            if (latitude != null && longitude != null) {
                lifecycleScope.launch {
                    interactor.saveHours(
                        latitude, longitude,
                        geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER
                    )
                }
            }
        } else {
            // Log the error.
            Log.e(APP_LOGTAG, "Invalid transition type $geofenceTransition")
        }
        return result
    }
}