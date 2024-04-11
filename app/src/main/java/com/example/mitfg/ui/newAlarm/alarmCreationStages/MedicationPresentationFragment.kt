package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentMedicationPresentationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicationPresentationFragment : Fragment(R.layout.fragment_medication_presentation) {

    private var _binding : FragmentMedicationPresentationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlarmCreationViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentMedicationPresentationBinding.bind(view)

        val callback = object : MedicinePresentationListAdapter.ItemClicked {
            override fun onClick(presentation: String) {
                viewModel.assignMedicinePresentation(presentation)
                viewModel.increaseProgressBar()

                navigateToNextScene()
            }
        }

        val items = listOf<String>(
            getString(R.string.tvPill),
            getString(R.string.tvPacket),
            getString(R.string.tvMililitres)
        )

        val adapter = MedicinePresentationListAdapter(callback)
        binding.medicinePresentationRecyclerView.adapter = adapter

        adapter.submitList(items)

        binding.buttonNext.setOnClickListener {
            viewModel.increaseProgressBar()

            navigateToNextScene()
        }

        binding.buttonBefore.setOnClickListener {
            viewModel.decreaseProgressBar()

            navigateBack()
        }
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.medicineSelectionFragment)
    }

    private fun navigateToNextScene() {
        findNavController().navigate(R.id.dosageSelectionFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}