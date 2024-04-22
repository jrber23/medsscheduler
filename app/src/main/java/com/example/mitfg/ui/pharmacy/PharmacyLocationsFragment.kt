package com.example.mitfg.ui.pharmacy

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentPharmacyLocationsBinding
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class PharmacyLocationsFragment : Fragment(R.layout.fragment_pharmacy_locations), OnMapReadyCallback, OnMyLocationClickListener, OnMyLocationButtonClickListener {

    private var _binding : FragmentPharmacyLocationsBinding? = null
    val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocation: FusedLocationProviderClient

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPharmacyLocationsBinding.bind(view)

        val mapFragment : SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        fusedLocation = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun isLocationPermissionsGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation() {
        if (!::mMap.isInitialized) return
        if (isLocationPermissionsGranted()) {
            mMap.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(requireContext(), "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = true
            } else {
                Toast.makeText(requireContext(), "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        if (!::mMap.isInitialized) return
        if (!isLocationPermissionsGranted()) {
            mMap.isMyLocationEnabled = false
            Toast.makeText(requireContext(), "Para activar la localización", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        fusedLocation.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val ubicacion = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 10f))
            }
        }

        enableLocation()

        val lat = 51.5074
        val lng = 0.1278
        val hash = GeoFireUtils.getGeoHashForLocation(GeoLocation(lat, lng))

        // Add the hash and the lat/lng to the document. We will use the hash
        // for queries and the lat/lng for distance comparisons.
        val updates: MutableMap<String, Any> = mutableMapOf(
            "geohash" to hash,
            "lat" to lat,
            "lng" to lng,
        )
        val londonRef = Firebase.firestore.collection("pharmacies").document("LON")
        londonRef.update(updates)
            .addOnCompleteListener {
                Log.d("PRUEBA_GEOHASHES", "Enhorabuena, se almacenó el hash")
            }

        val miUbi = LatLng(lat, lng)
        mMap.addMarker(MarkerOptions().position(miUbi).title("ESTO ES UNA PRUEBA IMPRESIONANTE!!").snippet("Y ESTO UN SNIPPET (no se lo que es)"))
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(requireContext(), "Current location:\n" + location, Toast.LENGTH_LONG)
            .show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(requireContext(), "MyLocation button clicked", Toast.LENGTH_SHORT)
            .show()

        return false
    }

}

