/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.medicineCreation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mitfg.data.adverseEffects.AdverseEffectRepository
import com.example.mitfg.data.medicines.MedicineRepository
import com.example.mitfg.domain.model.Medicine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * The medicine creation's view model that handles every data shown by the activity.
 */
@HiltViewModel
class MedicineCreationViewModel @Inject constructor(
    private val adverseEffectRepository: AdverseEffectRepository,
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    // StateFlow to hold the list of adverse effects
    private val _adverseEffectList = MutableStateFlow<List<String?>>(emptyList())
    val adverseEffectList : StateFlow<List<String?>> = _adverseEffectList.asStateFlow()

    // StateFlow to hold the selected adverse effects
    private val _selectedAdverseEffects = MutableStateFlow<List<String>>(ArrayList<String>())
    val selectedAdverseEffect : StateFlow<List<String>> = _selectedAdverseEffects.asStateFlow()

    // StateFlow to indicate if a medicine with the given name already exists
    private val _existsMedicine = MutableStateFlow<Boolean?>(null)
    val existsMedicine : StateFlow<Boolean?> = _existsMedicine.asStateFlow()

    // Fetches the list of all adverse effects when the ViewModel is initialized
    init {
        getAllAdverseEffects()
    }

    /**
     * Fetches the list of all adverse effects from the repository
     */
    private fun getAllAdverseEffects() {
        viewModelScope.launch {
            adverseEffectRepository.getAllAdverseEffects().fold(
                onSuccess = { list ->
                    _adverseEffectList.update { list.map { it?.name } }
                },
                onFailure = {
                    throwable -> Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }

    /**
     * Adds a new medicine to the repository
     * @param medicineName The medicine name
     * @param description The medicine description
     */
    fun addNewMedicine(medicineName: String, description: String) {
        val selectedEffects = _selectedAdverseEffects.value
        val newMedicine = Medicine("", medicineName, description, selectedEffects)

        viewModelScope.launch {
            medicineRepository.addNewMedicine(newMedicine)
        }
    }

    /**
     * Adds or removes an adverse effect from the selected list
     * @param selectedItem The selected value in the adverse effects list
     */
    fun addNewAdverseEffect(selectedItem: String) {
        if (!_selectedAdverseEffects.value.contains(selectedItem)) {
            _selectedAdverseEffects.update { newList ->
                newList + selectedItem
            }
        } else {
            _selectedAdverseEffects.update { newList ->
                newList - selectedItem
            }
        }
    }

    /**
     * Checks if a medicine with the given name already exists
     * @param medicineName The medicine name to check if the medicine already exists
     */
    fun existsNewMedicine(medicineName: String) {
        runBlocking {
            medicineRepository.existsMedicine(medicineName).fold(
                onSuccess = { exists ->
                    _existsMedicine.update { exists }
                },
                onFailure = { throwable ->
                    Log.d("FAILURE", throwable.toString())
                }
            )
        }
    }


}