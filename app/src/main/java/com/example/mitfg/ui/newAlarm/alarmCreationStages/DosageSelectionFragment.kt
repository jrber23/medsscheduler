package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.os.Bundle
import android.view.View
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

        binding.buttonNext.setOnClickListener {
            viewModel.decreseProgressBar()

            navigateBack()
        }
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.medicineSelectionFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}