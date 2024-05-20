package com.test.employeepresence.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.test.employeepresence.R
import com.test.employeepresence.databinding.FragmentSettingsBinding
import com.test.employeepresence.places.domain.WorkingPlace
import com.test.employeepresence.utils.APP_LOGTAG
import com.test.employeepresence.utils.GEOFENCE_RADIUS_IN_METERS
import com.test.employeepresence.utils.LocationPermissionChecker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(), OnMapReadyCallback {
    companion object {
        private const val DEFAULT_ZOOM = 15
        private val DEFAULT_LOCATION = LatLng(32.0730747, 34.7926483)
    }
    private var marker: Marker? = null
    private var geoFenceZone: Circle? = null

    private val viewModel: SettingsViewModel by viewModels()

    private var _binding: FragmentSettingsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGetLocation.setOnClickListener {
            getCurrentLocation()
        }

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        Log.d(APP_LOGTAG, "mapFragment $mapFragment")
        mapFragment?.getMapAsync(this)

        viewModel.placeLiveData.observe(viewLifecycleOwner) { location ->
            updateUI(location)
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateUI(workingPlace: WorkingPlace) {
        if (!::map.isInitialized) {
            // Переменная map не была инициализирована
            return
        }
        Log.d(APP_LOGTAG, "showOnMap $workingPlace")

        val latLng = LatLng(workingPlace.latitude, workingPlace.longitude)
        marker?.remove()
        geoFenceZone?.remove()
        map.isMyLocationEnabled = true

        marker = map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(getString(R.string.lbl_your_place)).snippet(workingPlace.address ?: "")
                .visible(true)
        ).also {
            it?.showInfoWindow()
            geoFenceZone = map.addCircle(
                CircleOptions()
                    .center(latLng)
                    .radius(GEOFENCE_RADIUS_IN_METERS.toDouble())
            )
        }
        setCameraPosition(latLng)

        binding.textViewAddress.text = workingPlace.address ?: getString(R.string.empty)
    }

    private fun getCurrentLocation() {
        if (LocationPermissionChecker.check(requireActivity()).isNotEmpty()) {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.requestCurrentLocation()
    }

    private fun setCameraPosition(latLng: LatLng) {
        if (!::map.isInitialized) {
            return
        }
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, DEFAULT_ZOOM.toFloat()))
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        Log.d(APP_LOGTAG, "onMapReady $map")
        setCameraPosition(DEFAULT_LOCATION)
        viewModel.placeLiveData.value?.let { updateUI(it) }
    }
}