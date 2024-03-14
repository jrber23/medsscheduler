package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentMedicationPresentationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicationPresentationFragment : Fragment(R.layout.fragment_medication_presentation) {

    private var _binding : FragmentMedicationPresentationBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentMedicationPresentationBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}