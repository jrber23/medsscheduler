package com.example.mitfg.ui.doctor

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentDoctorAppointmentsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DoctorAppointmentsFragment : Fragment(R.layout.fragment_doctor_appointments) {

    private var _binding : FragmentDoctorAppointmentsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DoctorAppointmentsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDoctorAppointmentsBinding.bind(view)

        val adapter = DoctorAppointmentsListAdapter()
        binding.recyclerViewDoctorAppointments.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.doctorAppointmentsList.collect { newList ->
                    adapter.submitList(newList)
                }
            }
        }


    }

}