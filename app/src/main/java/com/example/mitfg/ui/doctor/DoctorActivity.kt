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
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mitfg.databinding.ActivityDoctorBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 */
@AndroidEntryPoint
class DoctorActivity : AppCompatActivity() {

    // The controller that allow navigating between activities or fragments.
    private lateinit var navController: NavController

    /**
     * Method that launches when this activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setting up the nav controller
        navController = binding.doctorNavHostFragment.getFragment<NavHostFragment>().navController
        binding.doctorBottomNavigationView
        binding.doctorBottomNavigationView.setupWithNavController(navController)
    }
}