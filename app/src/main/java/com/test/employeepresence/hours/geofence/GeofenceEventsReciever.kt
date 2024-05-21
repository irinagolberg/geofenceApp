package com.test.employeepresence.hours.geofence

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.test.employeepresence.MainActivity
import com.test.employeepresence.R
import com.test.employeepresence.hours.domain.HoursInteractor
import com.test.employeepresence.utils.APP_LOGTAG
import com.test.employeepresence.utils.GEOFENCE_PLACE_EXTRA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceEventsReciever : LifecycleService() {
    @Inject
    lateinit var hoursInteractor: HoursInteractor

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        // If intent has the extra to stop the service
        if (intent?.getBooleanExtra(EXTRA_STOP_SERVICE, false) == true) {
            stopSelf()
        }
        val placeName = intent?.getStringExtra(GEOFENCE_PLACE_EXTRA) ?: getString(R.string.empty)

        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(intent) }
        if (geofencingEvent == null || geofencingEvent.hasError()) {
            geofencingEvent?.errorCode?.let {
                GeofenceStatusCodes
                    .getStatusCodeString(it)
                Log.e(APP_LOGTAG, "Geofence event error: $it")
            }
        } else {
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
                    sendNotification(placeName, geofenceTransition)
                    lifecycleScope.launch {
                        hoursInteractor.saveHours(
                            latitude, longitude,
                            geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER
                        )
                    }
                }
            } else {
                // Log the error.
                Log.e(APP_LOGTAG, "Invalid transition type $geofenceTransition")
            }
        }
        return START_STICKY
    }

    private fun sendNotification(placeName: String, transitionType: Int) {
        Log.d(APP_LOGTAG, "Geofence sendNotification $placeName: $transitionType")

        when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                val checkIn = getString(R.string.msg_check_in)
                notificationHelper.sendNotification(
                    checkIn,
                    placeName,
                    MainActivity::class.java
                )
            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                val checkOut = getString(R.string.msg_check_out)
                notificationHelper.sendNotification(
                    checkOut,
                    placeName,
                    MainActivity::class.java
                )
            }
        }

    }

    companion object {
        const val EXTRA_STOP_SERVICE = "EXTRA_STOP_SERVICE"
    }
}