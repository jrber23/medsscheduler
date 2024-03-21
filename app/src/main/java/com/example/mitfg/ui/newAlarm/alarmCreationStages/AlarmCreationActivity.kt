package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mitfg.databinding.ActivityAlarmCreationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlarmCreationActivity : AppCompatActivity() {

    private val viewModel: AlarmCreationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAlarmCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.progressBarValue.collect { newProgressBarValue ->
                binding.alarmCreationProgressBar.progress = newProgressBarValue
            }
        }

    }

    private fun createAlarm() {

    }


}