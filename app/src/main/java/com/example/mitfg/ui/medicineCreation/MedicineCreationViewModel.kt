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
import javax.inject.Inject


@HiltViewModel
class MedicineCreationViewModel @Inject constructor(
    private val adverseEffectRepository: AdverseEffectRepository,
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    private val _adverseEffectList = MutableStateFlow<List<String?>>(emptyList())
    val adverseEffectList : StateFlow<List<String?>> = _adverseEffectList.asStateFlow()

    private val _selectedAdverseEffects = MutableStateFlow<List<String>>(ArrayList<String>())
    val selectedAdverseEffect : StateFlow<List<String>> = _selectedAdverseEffects.asStateFlow()

    init {
        getAllAdverseEffects()
    }

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

    fun addNewMedicine(medicineName: String, description: String) {
        val newMedicine : Medicine = Medicine("", medicineName, description, _selectedAdverseEffects.value)

        viewModelScope.launch {
            medicineRepository.addNewMedicine(newMedicine)
        }
    }

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


}