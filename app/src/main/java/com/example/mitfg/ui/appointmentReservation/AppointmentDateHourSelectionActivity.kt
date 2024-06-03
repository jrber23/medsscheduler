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
import androidx.appcompat.app.AppCompatActivity
import com.example.mitfg.databinding.ActivityAppointmentDateHourSelectionBinding

/**
 * The activity to select the date and the hour appointment.
 */
class AppointmentDateHourSelectionActivity : AppCompatActivity() {

    // View binding instance
    private lateinit var _binding : ActivityAppointmentDateHourSelectionBinding
    private val binding get() = _binding

    /**
     * Called when the activity is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding and set the content view of the activity
        _binding = ActivityAppointmentDateHourSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}