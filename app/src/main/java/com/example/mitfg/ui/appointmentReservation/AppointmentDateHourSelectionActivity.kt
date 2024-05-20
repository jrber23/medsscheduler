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

class AppointmentDateHourSelectionActivity : AppCompatActivity() {

    private lateinit var _binding : ActivityAppointmentDateHourSelectionBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAppointmentDateHourSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}