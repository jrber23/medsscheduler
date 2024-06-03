/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.appointmentReservation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentAppointmentReservationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 *
 */
@AndroidEntryPoint
class AppointmentReservationFragment : Fragment(R.layout.fragment_appointment_reservation) {

    // View binding
    private var _binding : FragmentAppointmentReservationBinding? = null
    private val binding get() = _binding!!

    // ViewModel injection
    private val viewModel : DateTimeViewModel by activityViewModels()

    /**
     * Called when the fragment's view is created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAppointmentReservationBinding.bind(view)

        // Set click listener for the "Create New Appointment" button
        binding.createNewAppointmentButton.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show(parentFragmentManager, "datePicker")
        }

        // Initialize and set adapter for the RecyclerView
        val adapter = AppointmentListAdapter()
        binding.recyclerViewPendingAppointments.adapter = adapter

        // Observe appointment list changes and update the adapter
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appointmentList.collect { list ->
                    adapter.submitList(list)
                }
            }
        }
    }

    /**
     * Cleans up the view binding instance when the fragment's view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}