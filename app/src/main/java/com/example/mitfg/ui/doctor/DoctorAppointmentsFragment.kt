/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.doctor

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentDoctorAppointmentsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * The fragment that shows every appointment of a doctor
 */
@AndroidEntryPoint
class DoctorAppointmentsFragment : Fragment(R.layout.fragment_doctor_appointments) {

    // The binding that contains every reference to all the UI elements
    private var _binding : FragmentDoctorAppointmentsBinding? = null
    private val binding get() = _binding!!

    // The ViewModel instance of this fragment
    private val viewModel: DoctorAppointmentsViewModel by viewModels()

    /**
     * This method launches when the fragment is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDoctorAppointmentsBinding.bind(view)

        // Creates the adapter and attaches it to the recycler view
        val adapter = DoctorAppointmentsListAdapter()
        binding.recyclerViewDoctorAppointments.adapter = adapter

        // Every time a new list value is received, the recycler view is updated.
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.doctorAppointmentsList.collect { newList ->
                    adapter.submitList(newList)
                }
            }
        }


    }

}