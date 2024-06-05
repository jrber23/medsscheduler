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
 * The class that contains every action to be done when the alarm is triggered
 */
@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    // The alarm repository instance
    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    lateinit var notificationChannelManager: NotificationChannelManager

    /**
     * Called when the BroadcastReceiver is receiving an Intent broadcast.
     * @param context the context in which the receiver is running
     * @param intent the intent being received
     */
    override fun onReceive(context: Context, intent: Intent) {
        // Extract alarm details from the intent
        val idAlarm = intent.getLongExtra("idAlarm", -1)
        val medicineName = intent.getStringExtra("medicineName")
        val medicinePresentation = intent.getStringExtra("medicinePresentation")
        val dosage = intent.getStringExtra("dosage")

        // Update the alarm repository asynchronously
        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.addDosageToAlarm(idAlarm)
        }

        // Create and display a notification
        createSimpleNotification(idAlarm, medicineName, medicinePresentation, dosage)
    }

    /**
     * Creates and displays a simple notification.
     * @param context the context in which the receiver is running
     * @param idAlarm the ID of the alarm
     * @param medicineName the name of the medicine
     * @param medicinePresentation the presentation of the medicine
     * @param dosage the dosage of the medicine
     */
    private fun createSimpleNotification(
        idAlarm: Long,
        medicineName: String?,
        medicinePresentation: String?,
        dosage: String?
    ) {
        // Build the notification
        val alarmId : Int = idAlarm.toInt()

        val notification =
            notificationChannelManager
                .buildNewNotification(
                    alarmId,
                    medicineName,
                    medicinePresentation,
                    dosage
                )

        // Display the notification
        notificationChannelManager.displayNotification(alarmId, notification)
    }
}