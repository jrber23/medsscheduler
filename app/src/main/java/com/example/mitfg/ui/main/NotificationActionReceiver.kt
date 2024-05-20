/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.mitfg.data.instructionAlarm.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActionReceiver : BroadcastReceiver() {

    @Inject lateinit var alarmRepository: AlarmRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        val idAlarm = intent!!.getLongExtra("idAlarm", -1)

        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.addTakenDosageToAlarm(idAlarm)
        }
    }
}