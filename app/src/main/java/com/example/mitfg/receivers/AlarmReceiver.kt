/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.receivers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.mitfg.R
import com.example.mitfg.data.instructionAlarm.AlarmRepository
import com.example.mitfg.ui.main.MainActivity
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
        createSimpleNotification(context, idAlarm, medicineName, medicinePresentation, dosage)
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
        context: Context,
        idAlarm: Long,
        medicineName: String?,
        medicinePresentation: String?,
        dosage: String?
    ) {
        // Intent to open the MainActivity when the notification is clicked
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = "addTakenDosage"
            extras.apply {
                putExtra("idAlarm", idAlarm)
            }
        }

        // Create a pending intent for the notification action
        val flag = PendingIntent.FLAG_MUTABLE
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, idAlarm.toInt(), intent, flag)
        val vibrationPattern = longArrayOf(0, 2000, 1000, 500, 500, 500)

        // Build the notification
        val notification = NotificationCompat.Builder(context, MainActivity.MY_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_delete)
            .setContentTitle(context.getString(R.string.time_medicine))
            .setContentText(context.getString(R.string.time_medicine_message))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$medicineName - $dosage $medicinePresentation")
            )
            .addAction(R.drawable.medicines_icon, "DOSIS ADMINISTRADA", pendingIntent)
            .setVibrate(vibrationPattern)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Display the notification
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(idAlarm.toInt(), notification)
    }
}