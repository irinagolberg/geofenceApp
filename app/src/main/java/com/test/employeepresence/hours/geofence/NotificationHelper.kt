package com.test.employeepresence.hours.geofence

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.test.employeepresence.R
import com.test.employeepresence.utils.NOTIFICATION_CHANNEL_ID
import com.test.employeepresence.utils.NOTIFICATION_REQUEST_CODE
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Random
import javax.inject.Inject

class NotificationHelper @Inject constructor(@ApplicationContext val context: Context) : ContextWrapper(context) {
    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, getString(R.string.app_name), importance).apply {
                description = getString(R.string.channel_description)
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(title: String, msg: String, activityName: Class<*>?) {
        val intent = Intent(this, activityName)

        val flag = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            PendingIntent.FLAG_CANCEL_CURRENT
        } else {
            PendingIntent.FLAG_MUTABLE
        }
        val pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST_CODE, intent, flag)

        val notification =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_hours_24)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(msg)
                        .setBigContentTitle(title)
                )
                .setShowWhen(true)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(Random().nextInt(), notification)
        }
    }
}