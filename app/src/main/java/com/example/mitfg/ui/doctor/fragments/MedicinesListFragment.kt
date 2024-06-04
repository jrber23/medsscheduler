/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.doctor.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentMedicinesListBinding
import com.example.mitfg.ui.medicineCreation.MedicineCreationActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment that shows a medicines list.
 */
@AndroidEntryPoint
class MedicinesListFragment : Fragment(R.layout.fragment_medicines_list) {

    // View binding for the fragment layout
    private var _binding : FragmentMedicinesListBinding? = null
    private val binding get() = _binding!!

    /**
     * Called when the fragment's view has been created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMedicinesListBinding.bind(view)

        binding.createNewMedicine.setOnClickListener {
            swapToMedicineCreationActivity()
        }
    }

    /**
     * Navigates to the MedicineCreationActivity for creating a new medicine.
     */
    private fun swapToMedicineCreationActivity() {
        val destinationIntent = Intent(requireContext(), MedicineCreationActivity::class.java)
        startActivity(destinationIntent)
    }

    /**
     * Called when the fragment's view is destroyed.
     * Cleans up the view binding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}