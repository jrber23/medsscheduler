/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mitfg.databinding.ActivityDoctorSelectionBinding
import com.example.mitfg.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * The Doctor Selection activity that shows every content of doctor selection UI to the user.
 */
@AndroidEntryPoint
class DoctorSelectionActivity : AppCompatActivity() {

    // ViewModel instance for managing doctor selection-related data
    private val viewModel: DoctorSelectionViewModel by viewModels()

    // Binding object for accessing the views in the layout
    private lateinit var _binding : ActivityDoctorSelectionBinding

    // Callback interface implementation for handling item clicks in the doctor list
    private val callback =
        object : DoctorListAdapter.ItemClicked {
            override fun onClick(email: String) {
                // Update the selected doctor in the ViewModel
                viewModel.updateDoctor(email)

                // Navigate to the main activity
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)

                // Finish the current activity
                finish()
            }

        }

    /**
     * Called when the activity is first created. Initializes binding,
     * sets up the RecyclerView, and collects the doctor list from the ViewModel.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDoctorSelectionBinding.inflate(layoutInflater)

        val view = _binding.root
        setContentView(view)

        // Set up the RecyclerView with a LinearLayoutManager and the DoctorListAdapter
        val layoutManager = LinearLayoutManager(this)
        _binding.recyclerViewDoctors.layoutManager = layoutManager

        val adapter = DoctorListAdapter(callback)
        _binding.recyclerViewDoctors.adapter = adapter

        // Collect the list of doctors from the ViewModel and submit it to the adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.doctorList.collect { list ->
                    adapter.submitList(list)
                }
            }
        }
    }
}