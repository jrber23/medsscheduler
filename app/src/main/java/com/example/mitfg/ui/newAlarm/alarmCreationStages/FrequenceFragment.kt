package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentFrequenceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FrequenceFragment : Fragment(R.layout.fragment_frequence) {

    private var _binding : FragmentFrequenceBinding? = null
    private val binding get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFrequenceBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}