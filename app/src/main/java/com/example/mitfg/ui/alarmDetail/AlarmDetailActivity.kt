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
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mitfg.R
import com.example.mitfg.databinding.ActivityAlarmDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlarmDetailActivity : AppCompatActivity() {

    private lateinit var _binding : ActivityAlarmDetailBinding
    private val binding get() = _binding
    private val viewModel : AlarmDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAlarmDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idSelec = intent.getLongExtra("alarmId", 0)
        viewModel.getAlarmById(idSelec)

        lifecycleScope.launch {
            viewModel.alarm.collect { retrievedAlarm ->
                if (retrievedAlarm != null) {
                    viewModel.getMedicineByName(retrievedAlarm.medicineName)

                    binding.medicineTv.append("${retrievedAlarm.medicineName}")

                    binding.instructionTv.append(" ${retrievedAlarm.quantity} ")

                    when (retrievedAlarm.medicinePresentation) {
                        getString(R.string.pillAbrev) -> binding.instructionTv.append("${getString(R.string.showPill)} ${getString(R.string.everyWord)}")
                        getString(R.string.packetAbrev) -> binding.instructionTv.append("${getString(R.string.showPacket)} ${getString(R.string.everyWord)}")
                        getString(R.string.MlAbrev) -> binding.instructionTv.append("${getString(R.string.showMl)} ${getString(R.string.everyWord)}")
                    }

                    when (retrievedAlarm.frequency) {
                        AlarmManager.INTERVAL_DAY -> binding.instructionTv.append(" ${getString(R.string.hours24)}")
                        AlarmManager.INTERVAL_HALF_DAY -> binding.instructionTv.append(" ${getString(R.string.hours12)}")
                        AlarmManager.INTERVAL_HOUR -> binding.instructionTv.append(" ${getString(R.string.hours1)}")
                        AlarmManager.INTERVAL_HALF_HOUR -> binding.instructionTv.append(" ${getString(R.string.minutes30)}")
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES -> binding.instructionTv.append(" ${getString(R.string.minutes15)}")
                    }

                    binding.completedDosesTvTitle.append(" ${retrievedAlarm.totalDosages}")

                    binding.dosagesOnARowTvTitle.append(" ${retrievedAlarm.takenDosages}")
                }

            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.medicineAdverseEffects.collect { list ->
                    if (list != null) {

                        var text = ""
                        for ((index, element) in list.withIndex()) {
                            text = if (index == list.size - 1) {
                                "$element"
                            } else {
                                "$element, "
                            }

                            binding.secondaryEffectsDetails.append(text)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userCriticalInteractions.collect { list ->
                    if (list != null) {
                        var text = ""
                        for ((index, element) in list.withIndex()) {
                            text = if (index == list.size - 1) {
                                "$element"
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

    private fun appendCriticalInteractionsText() {
        val list = viewModel.userCriticalInteractions.value

        var text = ""
        for ((index, element) in list!!.withIndex()) {
            if (index == list.size - 1) {
                text = "$element"
            } else {
                text = "$element, "
            }

            binding.criticalInteractionsDetailTv.append(text)
        }
    }

    private fun paintText(text: String) {
        val spannableString = SpannableStringBuilder(text)

        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(applicationContext, R.color.red)),
            0,
            text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.secondaryEffectsDetails.append(spannableString)
    }
}

