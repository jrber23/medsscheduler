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

@AndroidEntryPoint
class MedicinesListFragment : Fragment(R.layout.fragment_medicines_list) {

    private var _binding : FragmentMedicinesListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMedicinesListBinding.bind(view)

        binding.createNewMedicine.setOnClickListener {
            swapToMedicineCreationActivity()
        }
    }

    private fun swapToMedicineCreationActivity() {
        val destinationIntent = Intent(requireContext(), MedicineCreationActivity::class.java)
        startActivity(destinationIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}