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
import android.util.Log
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

@AndroidEntryPoint
class DoctorSelectionActivity : AppCompatActivity() {

    private val viewModel: DoctorSelectionViewModel by viewModels()

    private lateinit var _binding : ActivityDoctorSelectionBinding

    private val callback =
        object : DoctorListAdapter.ItemClicked {
            override fun onClick(email: String) {
                viewModel.updateDoctor(email)

                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)

                finish()
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDoctorSelectionBinding.inflate(layoutInflater)

        val view = _binding.root
        setContentView(view)

        val layoutManager = LinearLayoutManager(this) // Puedes usar LinearLayoutManager u otro LayoutManager según tus necesidades
        _binding.recyclerViewDoctors.layoutManager = layoutManager

        val adapter = DoctorListAdapter(callback)
        _binding.recyclerViewDoctors.adapter = adapter

        Log.d("Recycler_VIEW", _binding.recyclerViewDoctors.adapter.toString())

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.doctorList.collect { list ->
                    adapter.submitList(list)
                }
            }
        }
    }
}