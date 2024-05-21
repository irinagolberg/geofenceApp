package com.test.employeepresence.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat

object LocationPermissionChecker {
    private val permissions: Array<String>
        get() {
            val permissions = mutableListOf<String>()

            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }

            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)

            return permissions.toTypedArray()
    }

    fun check(context: Context): Array<String> {
        val neededPermissions = mutableListOf<String>()

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(APP_LOGTAG, "checkPermission: $permission DENIED")
                neededPermissions.add(permission)
            }
        }
        return neededPermissions.toTypedArray()
    }


}