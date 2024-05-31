/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.alarmDetail

import android.app.AlarmManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mitfg.R
import com.example.mitfg.databinding.ActivityAlarmDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * The alarm detail activity that shows every content of alarm detail UI to the user.
 */
@AndroidEntryPoint
class AlarmDetailActivity : AppCompatActivity() {

    // ViewBinding variables
    private lateinit var _binding : ActivityAlarmDetailBinding
    private val binding get() = _binding

    // ViewModel instance
    private val viewModel : AlarmDetailViewModel by viewModels()

    /**
     * Called when the activity is first created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflating layout using ViewBinding
        _binding = ActivityAlarmDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieving alarm ID from intent extras
        val idSelectedAlarm = intent.getLongExtra("alarmId", 0)
        viewModel.getAlarmById(idSelectedAlarm)

        // Observing alarm data changes
        lifecycleScope.launch {
            viewModel.alarm.collect { retrievedAlarm ->
                if (retrievedAlarm != null) {
                    // Updating UI with alarm details
                    viewModel.getMedicineByName(retrievedAlarm.medicineName)

                    binding.medicineTv.append(retrievedAlarm.medicineName)

                    binding.instructionTv.append(" ${retrievedAlarm.quantity} ")

                    when (retrievedAlarm.medicinePresentation) {
                        getString(R.string.pillAbrev) -> binding.instructionTv.append("${getString(R.string.showPill)} ${getString(R.string.everyWord)}")
                        getString(R.string.packetAbrev) -> binding.instructionTv.append("${getString(R.string.showPacket)} ${getString(R.string.everyWord)}")
                        getString(R.string.MlAbrev) -> binding.instructionTv.append("${getString(R.string.showMl)} ${getString(R.string.everyWord)}")
                    }

                    // Appending frequency details
                    when (retrievedAlarm.frequency) {
                        AlarmManager.INTERVAL_DAY -> binding.instructionTv.append(" ${getString(R.string.hours24)}")
                        AlarmManager.INTERVAL_HALF_DAY -> binding.instructionTv.append(" ${getString(R.string.hours12)}")
                        AlarmManager.INTERVAL_HOUR -> binding.instructionTv.append(" ${getString(R.string.hours1)}")
                        AlarmManager.INTERVAL_HALF_HOUR -> binding.instructionTv.append(" ${getString(R.string.minutes30)}")
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES -> binding.instructionTv.append(" ${getString(R.string.minutes15)}")
                    }

                    // Appending completed and taken dosages
                    binding.completedDosesTvTitle.append(" ${retrievedAlarm.totalDosages}")
                    binding.dosagesOnARowTvTitle.append(" ${retrievedAlarm.takenDosages}")
                }

            }
        }

        // Observing medicine adverse effects
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.medicineAdverseEffects.collect { list ->
                    if (list != null) {

                        var text = ""
                        for ((index, element) in list.withIndex()) {
                            text = if (index == list.size - 1) {
                                element
                            } else {
                                "$element, "
                            }

                            binding.secondaryEffectsDetails.append(text)
                        }
                    }
                }
            }
        }

        // Observing user critical interactions
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userCriticalInteractions.collect { criticalInteractionsList ->
                    if (criticalInteractionsList != null) {
                        var text = ""
                        for ((index, element) in criticalInteractionsList.withIndex()) {
                            text = if (index == criticalInteractionsList.size - 1) {
                                element
                            } else {
                                "$element, "
                            }

                            binding.criticalInteractionsDetailTv.append(text)
                        }
                    }
                }
            }
        }
    }
}

