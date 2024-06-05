/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.newAlarm.alarmCreationStages

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mitfg.R
import com.example.mitfg.databinding.ActivityAlarmCreationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * The activity that manages the new alarm state and the progress bar value
 */
@AndroidEntryPoint
class AlarmCreationActivity : AppCompatActivity() {

    // The ViewModel instance of this activity
    private val viewModel: AlarmCreationViewModel by viewModels()

    /**
     * Method that launches when this activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAlarmCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Updates the progress bar value every time a stage is completed
        lifecycleScope.launch {
            viewModel.progressBarValue.collect { newProgressBarValue ->
                binding.alarmCreationProgressBar.progress = newProgressBarValue
            }
        }

        lifecycleScope.launch {
            viewModel.alarm.collect { newAlarmValue ->
                val medicinePresentationEntire = when (newAlarmValue.medicinePresentation) {
                    getString(R.string.pillAbrev) -> getString(R.string.showPill)
                    getString(R.string.packetAbrev) -> getString(R.string.showPacket)
                    getString(R.string.MlAbrev) -> getString(R.string.showMl)
                    else -> { "" }
                }

                binding.alarmSummarizeTv.text = "${newAlarmValue.medicineName} - ${newAlarmValue.quantity} ${medicinePresentationEntire}"
            }
        }
    }
}