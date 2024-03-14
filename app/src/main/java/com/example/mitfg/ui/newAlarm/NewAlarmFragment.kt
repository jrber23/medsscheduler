package com.example.mitfg.ui.newAlarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentNewAlarmBinding
import com.example.mitfg.ui.newAlarm.alarmCreationStages.AlarmCreationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewAlarmFragment : Fragment(R.layout.fragment_new_alarm) {

    private var _binding : FragmentNewAlarmBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewAlarmBinding.bind(view)

        binding.createNewAlarm.setOnClickListener {
            swapToCreateAlarmActivity()
        }
    }

    private fun swapToCreateAlarmActivity() {
        val intent = Intent(context, AlarmCreationActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}