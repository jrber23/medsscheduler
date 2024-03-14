package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentMedicineSelectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicineSelectionFragment : Fragment(R.layout.fragment_medicine_selection) {

    private var _binding : FragmentMedicineSelectionBinding? = null

    private val viewModel: AlarmCreationViewModel by activityViewModels()
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMedicineSelectionBinding.bind(view)

        binding.button.setOnClickListener {
            viewModel.increseProgressBar()

            navigateToNextScene()
        }
    }

    private fun navigateToNextScene() {
        findNavController().navigate(R.id.dosageSelectionFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}