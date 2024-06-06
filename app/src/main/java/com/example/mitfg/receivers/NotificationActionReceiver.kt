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
import com.example.mitfg.utils.NotificationChannelManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The class that contains every action to be done when the notification button is pressed
 */
@AndroidEntryPoint
class NotificationActionReceiver : BroadcastReceiver() {

    // The alarm repository instance
    @Inject lateinit var alarmRepository: AlarmRepository

    @Inject lateinit var notificationChannelManager: NotificationChannelManager

    /**
     * Called when the BroadcastReceiver is receiving an Intent broadcast.
     * @param context the context in which the receiver is running
     * @param intent the intent being received
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        // Extract the alarm ID from the intent
        val idAlarm = intent!!.getLongExtra("idAlarm", -1)
        // val idAlarmInt = intent.getIntExtra("idAlarm", -1)

        // Update the alarm repository asynchronously
        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.addTakenDosageToAlarm(idAlarm)

            // notificationChannelManager.cancelNotification(idAlarmInt)
        }
    }
}