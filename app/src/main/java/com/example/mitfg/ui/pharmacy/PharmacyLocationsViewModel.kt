/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.pharmacy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitfg.data.pharmacies.PharmacyRepository
import com.example.mitfg.domain.model.Pharmacy
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * The PharmacyLocationsFragment's view model. It contains every data that handles the fragment.
 */
@HiltViewModel
class PharmacyLocationsViewModel @Inject constructor(
    private val pharmacyRepository: PharmacyRepository
) : ViewModel() {

    // StateFlow to hold the current location (ubication).
    private val _ubication = MutableStateFlow<LatLng?>(null)
    val ubication : StateFlow<LatLng?> = _ubication.asStateFlow()

    // StateFlow to hold the list of pharmacies.
    private val _pharmaciesList = MutableStateFlow<List<Pharmacy?>>(emptyList())
    val pharmacyList : StateFlow<List<Pharmacy?>> = _pharmaciesList.asStateFlow()

    /**
     * Private function to fetch all pharmacies within a certain radius based on the current location.
     * @param ubication the latitude and longitude data for collecting every near pharmacy
     */
    private fun getAllPharmaciesWithinRadius(ubication: LatLng) {
        viewModelScope.launch {
            pharmacyRepository.getAllPharmaciesWithinRadius(ubication).fold(
                onSuccess = { list ->
                    _pharmaciesList.update { list }
                },
                onFailure = { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }

    /**
     * Function to update the current location and fetch nearby pharmacies.
     * @param ubication the new ubication value
     */
    fun updateUbication(ubication: LatLng) {
        _ubication.update { ubication }

        getAllPharmaciesWithinRadius(_ubication.value!!)
    }

}