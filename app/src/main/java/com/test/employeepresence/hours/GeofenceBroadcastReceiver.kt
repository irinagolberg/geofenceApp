package com.test.employeepresence.hours

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.test.employeepresence.hours.domain.HoursRecord
import com.test.employeepresence.hours.domain.HoursRecordType
import com.test.employeepresence.utils.APP_LOGTAG
import kotlinx.coroutines.launch
import java.util.Calendar

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    //@Inject lateinit var interactor: HoursInteractor
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(APP_LOGTAG, "Geofence intent: $intent")
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent == null || geofencingEvent.hasError()) {
            geofencingEvent?.errorCode?.let {
                GeofenceStatusCodes
                    .getStatusCodeString(it)
                Log.e(APP_LOGTAG, "Geofence event error: $it")
            }
            return
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
                /*lifecycleScope.launch {
                    interactor.saveHours(
                        latitude, longitude,
                        geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER
                    )
                }*/
            }
        } else {
            // Log the error.
            Log.e(APP_LOGTAG, "Invalid transition type $geofenceTransition")
        }
    }

}
