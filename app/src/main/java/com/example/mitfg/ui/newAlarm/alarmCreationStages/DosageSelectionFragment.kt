package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentDosageSelectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DosageSelectionFragment : Fragment(R.layout.fragment_dosage_selection) {

    private var _binding : FragmentDosageSelectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlarmCreationViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDosageSelectionBinding.bind(view)

        val spinner: Spinner = binding.pillQuantitySpinner

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.assignPillQuantity(parent?.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val medicinePresentation = viewModel.getMedicinePresentation()

        when (medicinePresentation) {
            getString(R.string.pillAbrev) -> {
                binding.tvDosage.text = getString(R.string.pill)

                ArrayAdapter.createFromResource(
                    requireActivity().baseContext,
                    R.array.pill_quantity_array,
                    android.R.layout.simple_spinner_item
                ).also { arrayAdapter ->
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = arrayAdapter
                }
            }

            getString(R.string.packetAbrev) -> {
                binding.tvDosage.text = getString(R.string.packet)

                ArrayAdapter.createFromResource(
                    requireActivity().baseContext,
                    R.array.packet_quantity_array,
                    android.R.layout.simple_spinner_item
                ).also { arrayAdapter ->
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = arrayAdapter
                }
            }

            getString(R.string.MlAbrev) -> {
                binding.tvDosage.text = getString(R.string.mililitres)

                ArrayAdapter.createFromResource(
                    requireActivity().baseContext,
                    R.array.mililiters_quantity_array,
                    android.R.layout.simple_spinner_item
                ).also { arrayAdapter ->
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = arrayAdapter
                }
            }
        }

        spinner.onItemSelectedListener = listener

        binding.buttonNext.setOnClickListener {
            viewModel.increaseProgressBar()

            navigateForward()
        }

        binding.buttonBefore.setOnClickListener {
            viewModel.decreaseProgressBar()

            navigateBack()
        }
    }

    private fun assignPillQuantity() {
        viewModel.assignPillQuantity(binding.pillQuantitySpinner.selectedItem.toString())
    }

    private fun navigateForward() {
        assignPillQuantity()

        findNavController().navigate(R.id.frequenceFragment)
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.medicationPresentationFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}