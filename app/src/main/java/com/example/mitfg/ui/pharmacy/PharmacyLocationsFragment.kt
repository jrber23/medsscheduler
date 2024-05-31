/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.pharmacy

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentPharmacyLocationsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * The fragment to show a map with all the neareast pharmacy's location
 */
@AndroidEntryPoint
class PharmacyLocationsFragment : Fragment(R.layout.fragment_pharmacy_locations), OnMapReadyCallback {

    private var _binding : FragmentPharmacyLocationsBinding? = null
    val binding get() = _binding!!

    // The map shown on the screen
    private lateinit var mMap: GoogleMap

    // Object to receive the user's location
    private lateinit var fusedLocation: FusedLocationProviderClient

    private val viewModel : PharmacyLocationsViewModel by viewModels()

    // Object with a value that represents the code of the location request
    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    /**
     * This method is called when the fragment's view is created.
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPharmacyLocationsBinding.bind(view)

        // Set up the map fragment and get notified when the map is ready to use.
        (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(this)

        // Initialize the fused location provider client.
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Observe the pharmacy list from the view model and add markers to the map for each pharmacy.
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pharmacyList.collect { pharmaciesList ->
                    if (::mMap.isInitialized) {
                        for (element in pharmaciesList) {
                            val coordinates = LatLng(element!!.lat, element.lng)

                            mMap.addMarker(
                                MarkerOptions().position(coordinates).title(element.pharmacyName)
                                    .snippet("${element.address} - ${element.city} - ${element.region}")
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if location permissions are granted.
     */
    private fun isLocationPermissionsGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    /**
     * Enable or disable location services on the map.
     */
    private fun enableLocation(enabledLocation: Boolean) {
        if (!::mMap.isInitialized) return
        if (isLocationPermissionsGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    REQUEST_CODE_LOCATION
                )
                return
            }
            mMap.isMyLocationEnabled = enabledLocation

            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        } else {
            requestLocationPermission()
        }
    }

    /**
     * Request location permission from the user.
     */
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(requireContext(), "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    /**
     * Handle the result of the permission request.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation(true)
            } else {
                Toast.makeText(requireContext(), "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    /**
     *  This method is called when the fragment is resumed.
     */
    override fun onResume() {
        super.onResume()

        enableLocation(false)
    }

    /**
     * This method is called when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * This method is called when the map is ready to be used.
     * @param googleMap The retrieved reference to the Google Maps instance
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION
            )
            return
        }

        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        // Get the last known location and center the map on it.
        fusedLocation.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val ubication = LatLng(location.latitude, location.longitude)

                updateUbication(ubication)

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubication, 15f))
            }
        }

        enableLocation(true)


    }

    /**
     * Update the user's location in the view model
     * @param ubication the latitude and longitude data of the user
     */
    private fun updateUbication(ubication: LatLng) {
        viewModel.updateUbication(ubication)
    }

}

