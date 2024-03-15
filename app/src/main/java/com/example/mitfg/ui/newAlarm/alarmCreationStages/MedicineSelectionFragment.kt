package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentMedicineSelectionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MedicineSelectionFragment : Fragment(R.layout.fragment_medicine_selection) {

    private var _binding : FragmentMedicineSelectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlarmCreationViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMedicineSelectionBinding.bind(view)
        val adapter: MedicineListAdapter = MedicineListAdapter()
        binding.recyclerViewMedicines.adapter = adapter

        lifecycleScope.launch {
            viewModel.medicinesList.collect { list ->
                adapter.submitList(list)
            }
        }

        binding.buttonNext.setOnClickListener {
            viewModel.increseProgressBar()

            navigateToNextScene()
        }
    }

    private fun navigateToNextScene() {
        findNavController().navigate(R.id.medicationPresentationFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}