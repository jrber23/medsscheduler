/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.mitfg.R
import com.example.mitfg.ui.main.MainActivity
import javax.inject.Inject

class NotificationChannelManager @Inject constructor(
    private val context: Context
) {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Object with an ID to identify the notification channel
    private companion object {
        const val MY_NOTIFICATION_CHANNEL_ID = "myChannel"
    }

    fun createNotificationChannel() {
        val channel = NotificationChannel(
            MY_NOTIFICATION_CHANNEL_ID,
            "MySuperChannel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "SUSCRIBETE"
        }

        notificationManager.createNotificationChannel(channel)
    }

    fun displayNotification(alarmId: Int, notification: Notification) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(alarmId, notification)
    }

    fun buildNewNotification(
        idAlarm: Int,
        medicineName: String?,
        medicinePresentation: String?,
        dosage: String?) : Notification {
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
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, idAlarm, intent, flag)
        val vibrationPattern = longArrayOf(0, 2000, 1000, 500, 500, 500)

        // Build the notification
        val notification = NotificationCompat.Builder(context, MY_NOTIFICATION_CHANNEL_ID)
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

        return notification
    }

}