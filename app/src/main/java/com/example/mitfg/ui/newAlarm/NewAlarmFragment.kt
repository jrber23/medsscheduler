package com.example.mitfg.ui.newAlarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mitfg.R
import com.example.mitfg.databinding.FragmentNewAlarmBinding
import com.example.mitfg.ui.newAlarm.alarmCreationStages.AlarmCreationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewAlarmFragment : Fragment(R.layout.fragment_new_alarm) {

    private var _binding : FragmentNewAlarmBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewAlarmViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewAlarmBinding.bind(view)

        binding.createNewAlarm.setOnClickListener {
            swapToCreateAlarmActivity()
        }

        val adapter = AlarmListAdapter()
        binding.recyclerViewAlarms.adapter = adapter

        lifecycleScope.launch {
            viewModel.alarmList.collect { list ->
                adapter.submitList(list)
            }
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