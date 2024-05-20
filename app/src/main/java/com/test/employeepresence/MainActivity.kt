package com.test.employeepresence

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.test.employeepresence.databinding.ActivityMainBinding
import com.test.employeepresence.utils.APP_LOGTAG
import com.test.employeepresence.utils.LocationPermissionChecker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_hours, R.id.navigation_stats, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Toast.makeText(this, "${intent.action}", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        requestLocationPermissions()
    }

    private fun requestLocationPermissions() {
        val notPermissionGranted = LocationPermissionChecker.check(this)
        Log.d(APP_LOGTAG, "requestLocationPermissions: ${notPermissionGranted.toList()}")
        notPermissionGranted.firstOrNull()?.let { permission ->
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    notPermissionGranted.first()
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(R.string.location_permission_rationale)
                    .setCancelable(false)
                    .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                        // Requesting foreground location permission
                        requestLocationPermission(permission)
                    }
                    .setNegativeButton(android.R.string.cancel) { _, _ ->
                        processPermissions()
                    }
                    .show()
            } else {
                // Requesting location permission
                requestLocationPermission(permission)
            }
        } ?: processPermissions()

    }

    private fun requestLocationPermission(permission: String) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_LOCATION_PERMISSIONS)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSIONS) {
            processPermissions()
        }
    }

    private fun processPermissions() {
        binding.container.visibility = if (LocationPermissionChecker.check(this).isEmpty()) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE.also {
                requestLocationPermissions()
            }
        }
    }

    companion object {
        const val REQUEST_LOCATION_PERMISSIONS = 10005
    }
}