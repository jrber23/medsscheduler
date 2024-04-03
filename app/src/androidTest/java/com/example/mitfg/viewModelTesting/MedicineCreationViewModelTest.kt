package com.example.mitfg.viewModelTesting

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mitfg.data.FakeAdverseEffectRepository
import com.example.mitfg.data.FakeMedicineRepository
import com.example.mitfg.domain.model.Medicine
import com.example.mitfg.ui.medicineCreation.MedicineCreationViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MedicineCreationViewModelTest {

    private lateinit var adverseEffectRepository: FakeAdverseEffectRepository
    private lateinit var medicineRepository: FakeMedicineRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        adverseEffectRepository = FakeAdverseEffectRepository()
        medicineRepository = FakeMedicineRepository()
    }

    @Test
    fun getAllAdverseEffects_Test() = runTest {
        // Given
        val medicineCreationViewModel = MedicineCreationViewModel(
            adverseEffectRepository = adverseEffectRepository,
            medicineRepository = medicineRepository
        )

        // When
        backgroundScope.launch {
            medicineCreationViewModel.adverseEffectList.collect { listOfAdverseEffects ->
                // Then
                assertTrue(listOfAdverseEffects.isNotEmpty())
                assertEquals(3, listOfAdverseEffects.size)
            }
        }
    }

    @Test
    fun addNewMedicine_Test() = runTest {
        // Given
        val medicineCreationViewModel = MedicineCreationViewModel(
            adverseEffectRepository = adverseEffectRepository,
            medicineRepository = medicineRepository
        )

        val newMedicine = Medicine("4", "Flutox", "Para la tos", emptyList())

        // When
        backgroundScope.launch {
            medicineCreationViewModel.addNewMedicine(newMedicine.name, newMedicine.description)

            // Then
            medicineRepository.getAllMedicines().fold(
                onSuccess = { medicinesList ->
                    assertEquals(5, medicinesList.size)
                },
                onFailure = { throwable ->
                    Log.d("EXCEPTION", throwable.toString())
                }
            )
        }
    }

    @Test
    fun addNewAdverseEffect_Test() = runTest {
        // Given
        val medicineCreationViewModel = MedicineCreationViewModel(
            adverseEffectRepository = adverseEffectRepository,
            medicineRepository = medicineRepository
        )

        backgroundScope.launch {
            // When
            medicineCreationViewModel.addNewAdverseEffect("Paranoia")

            // Then
            medicineCreationViewModel.selectedAdverseEffect.collect { modifiedList ->
                assertTrue(modifiedList.contains("Paranoia"))
            }
        }
    }

}