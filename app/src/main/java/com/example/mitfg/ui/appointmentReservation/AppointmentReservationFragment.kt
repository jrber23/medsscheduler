package com.example.mitfg.ui.appointmentReservation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentAppointmentReservationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppointmentReservationFragment : Fragment(R.layout.fragment_appointment_reservation) {

    private var _binding : FragmentAppointmentReservationBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAppointmentReservationBinding.bind(view)

        binding.createNewAppointmentButton.setOnClickListener {
            /* val intent = Intent(requireContext(), DatePickerFragment::class.java)
            startActivity(intent) */

            val newFragment = DatePickerFragment()
            newFragment.show(parentFragmentManager, "datePicker")
        }

    }
}