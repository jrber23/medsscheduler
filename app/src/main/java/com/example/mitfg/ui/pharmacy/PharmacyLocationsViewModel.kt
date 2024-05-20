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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PharmacyLocationsViewModel @Inject constructor(
    private val pharmacyRepository: PharmacyRepository
) : ViewModel() {

    private val _ubication = MutableStateFlow<com.google.android.gms.maps.model.LatLng?>(null)
    val ubication : StateFlow<com.google.android.gms.maps.model.LatLng?> = _ubication.asStateFlow()

    private val _pharmaciesList = MutableStateFlow<List<Pharmacy?>>(emptyList())
    val pharmacyList : StateFlow<List<Pharmacy?>> = _pharmaciesList.asStateFlow()

    private fun getAllPharmaciesWithinRadius(ubication: com.google.android.gms.maps.model.LatLng) {
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

    fun updateUbication(ubication: com.google.android.gms.maps.model.LatLng) {
        _ubication.update { ubication }

        getAllPharmaciesWithinRadius(_ubication.value!!)
    }

}