package com.example.mitfg.viewModelTesting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mitfg.data.FakePharmacyRepository
import com.example.mitfg.data.pharmacies.PharmacyRepository
import com.example.mitfg.ui.pharmacy.PharmacyLocationsViewModel
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PharmacyViewModelTest {

    private lateinit var pharmacyRepository: PharmacyRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        pharmacyRepository = FakePharmacyRepository()
    }

    @Test
    fun getAllPharmacyLocationsWithinRadius_Test() = runTest {
        // Given
        val pharmacyLocationsViewModel = PharmacyLocationsViewModel(
            pharmacyRepository = pharmacyRepository
        )

        val ubication = com.google.android.gms.maps.model.LatLng(10.0, 25.0)


        backgroundScope.launch {
            // When
            pharmacyLocationsViewModel.updateUbication(ubication)

            // Then
            pharmacyLocationsViewModel.pharmacyList.collect { pharmaciesList ->
                assertTrue(pharmaciesList.isNotEmpty())
            }
        }



    }


}