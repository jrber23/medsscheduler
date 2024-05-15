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

@AndroidEntryPoint
class MedicineCreationActivity : AppCompatActivity() {

    private val viewModel: MedicineCreationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMedicineCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.adverseEffectList.collect { list ->
                    val arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_multiple_choice, list)

                    binding.listView.adapter = arrayAdapter
                }
            }
        }

        binding.listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()

            viewModel.addNewAdverseEffect(selectedItem)
        }

        binding.bCrearMedicamento.setOnClickListener {
            val medicineName = binding.etMedicineName.text.toString()
            val description = binding.etMedicineDescription.text.toString()

            if (!thereAreEmptyFields(medicineName, description)) {
                viewModel.addNewMedicine(medicineName, description)

                Toast.makeText(applicationContext, "Se ha insertado una nueva medicina", Toast.LENGTH_SHORT).show()
            } else {
                val message = getString(R.string.emptyFieldsMedicineCreation)

                showPopUp(message)
            }
        }
    }

    private fun thereAreEmptyFields(medicineName: String, medicineDescription: String): Boolean {
        return (medicineName.isEmpty() || medicineDescription.isEmpty())
    }

    fun showPopUp(message: String) {
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