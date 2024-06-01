/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.medicineCreation

import android.content.DialogInterface
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mitfg.R
import com.example.mitfg.databinding.ActivityMedicineCreationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * The medicine creation activity that shows every content of medicine creation UI to the user.
 */
@AndroidEntryPoint
class MedicineCreationActivity : AppCompatActivity() {

    // ViewModel instance for managing medicine creation-related data
    private val viewModel: MedicineCreationViewModel by viewModels()

    /**
     * Called when the activity is first created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using view binding
        val binding = ActivityMedicineCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observes the adverse effect list from the ViewModel and updates the ListView accordingly
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.adverseEffectList.collect { list ->
                    val arrayAdapter = ArrayAdapter(applicationContext, R.layout.single_list_item_multiple_choice_custom, list)

                    binding.listView.adapter = arrayAdapter
                }
            }
        }

        // Handles item clicks on the ListView to add selected adverse effects to the ViewModel
        binding.listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()

            viewModel.addNewAdverseEffect(selectedItem)
        }

        // Handles button click to create a new medicine
        binding.bCrearMedicamento.setOnClickListener {
            // Get input values
            val medicineName = binding.etMedicineName.text.toString()
            val description = binding.etMedicineDescription.text.toString()

            // Checks if a medicine with the given name exists
            viewModel.existsNewMedicine(medicineName)

            // Check for empty fields. If there are any empty field, a message error is shown.
            if (!thereAreEmptyFields(medicineName, description)) {
                // If the medicine already exists, shows a message error
                if (viewModel.existsMedicine.value != null) {
                    if (!viewModel.existsMedicine.value!!) {
                        viewModel.addNewMedicine(medicineName, description)

                        Toast.makeText(applicationContext, R.string.new_medicine_added, Toast.LENGTH_SHORT).show()
                    } else {
                        val message = getString(R.string.existing_medicine_name)

                        showPopUp(message)
                    }
                }
            } else {
                val message = getString(R.string.emptyFieldsMedicineCreation)

                showPopUp(message)
            }
        }
    }

    /**
     * Checks if there are empty fields
     * @param medicineName The string extracted from the medicine name field
     * @param medicineDescription The string extracted from the medicine description field
     * @return true if there are any empty field, and false, otherwise
     */
    private fun thereAreEmptyFields(medicineName: String, medicineDescription: String): Boolean {
        return (medicineName.isEmpty() || medicineDescription.isEmpty())
    }

    /**
     * Shows a popup dialog with the given message
     * @param message The message to display in the popup
     */
    private fun showPopUp(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(R.string.accept, DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            })
        val alert = builder.create()
        alert.show()
    }
}