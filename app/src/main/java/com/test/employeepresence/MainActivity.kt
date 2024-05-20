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

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val notGranted = permissions.filter { !it.value }

        if (notGranted.isNotEmpty()) {
            Log.d(APP_LOGTAG, "Location permissions not granted $notGranted")
            processPermissions(false)
        } else {
            processPermissions(true)
        }
    }

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

        if (notPermissionGranted.isNotEmpty()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, notPermissionGranted.first())) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(R.string.location_permission_rationale)
                    .setCancelable(false)
                    .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                        // Requesting foreground location permission
                        locationPermissionLauncher.launch(
                            arrayOf(notPermissionGranted.first())
                        )
                    }
                    .setNegativeButton(android.R.string.cancel) { _, _ ->
                        processPermissions(false)
                    }
                    .show()
                return
            } else {
                // Requesting location permission
                locationPermissionLauncher.launch(
                    arrayOf(notPermissionGranted.first())
                )
            }
        } else {
            processPermissions(true)
        }

    }

    private fun processPermissions(granted: Boolean) {
        when (granted) {
            true -> {
                binding.container.visibility = android.view.View.VISIBLE
            }
            false -> {
                binding.container.visibility = android.view.View.GONE
            }
        }
    }
}