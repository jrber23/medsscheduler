/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.mitfg.data.instructionAlarm.AlarmRepository
import com.example.mitfg.utils.AlarmManagerHelper
import com.example.mitfg.utils.NotificationChannelManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The class that contains every action to be done when the device has been rebooted
 */
@AndroidEntryPoint
class RebootReceiver : BroadcastReceiver() {

    // The alarm repository instance
    @Inject
    lateinit var alarmRepository: AlarmRepository

    // The alarm manager helper instance
    @Inject
    lateinit var alarmManagerHelper: AlarmManagerHelper

    // The notification channel manager instance
    @Inject
    lateinit var notificationChannelManager: NotificationChannelManager

    /**
     * Called when the BroadcastReceiver is receiving an Intent broadcast.
     * @param context the context in which the receiver is running
     * @param intent the intent being received
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        // Check if the received intent action is BOOT_COMPLETED
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {

            notificationChannelManager.createNotificationChannel()

            var haveBeenExecuted = false

            // Launch a coroutine to fetch all alarms and reschedule them
            CoroutineScope(Dispatchers.IO).launch {
                alarmRepository.getAllAlarms().collect { receivedList ->
                    if (!haveBeenExecuted) {
                        for (alarm in receivedList) {
                            alarmManagerHelper.createAlarm(alarm)
                        }
                        haveBeenExecuted = true
                    }
                }
            }
        }
    }
}