package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.mitfg.databinding.ActivityAlarmCreationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmCreationActivity : AppCompatActivity() {

    private val viewModel: AlarmCreationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAlarmCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val progressBar = binding.alarmCreationProgressBar

        viewModel.progressBarValue.observe(this, Observer {
            progressBar.progress = viewModel.progressBarValue.value!!
        })

    }


}