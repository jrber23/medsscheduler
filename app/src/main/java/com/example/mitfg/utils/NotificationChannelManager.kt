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

/**
 * Object that manages the aplication notifications channel.
 * It's here where all the notifications are created and launched.
 */
class NotificationChannelManager @Inject constructor(
    private val context: Context
) {

    // Initialize NotificationManager using the system service
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Object with an ID to identify the notification channel
    private companion object {
        const val MY_NOTIFICATION_CHANNEL_ID = "myChannel"
    }

    /**
     * Creates a notification channel for the application.
     * This is necessary for API level 26 and above.
     */
    fun createNotificationChannel() {
        // Create a notification channel with specified ID, name, and importance level
        val channel = NotificationChannel(
            MY_NOTIFICATION_CHANNEL_ID,
            "MySuperChannel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            // Set the channel description
            description = "SUSCRIBETE"
        }

        // Register the channel with the system
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Displays a notification.
     *
     * @param alarmId The unique ID for the notification.
     * @param notification The Notification object to be displayed.
     */
    fun displayNotification(alarmId: Int, notification: Notification) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(alarmId, notification)
    }

    /**
     * Builds a new notification.
     *
     * @param idAlarm The ID of the alarm.
     * @param medicineName The name of the medicine.
     * @param medicinePresentation The presentation form of the medicine (e.g., pill, packet).
     * @param dosage The dosage of the medicine.
     * @return A Notification object configured with the provided details.
     */
    fun buildNewNotification(
        idAlarm: Int,
        medicineName: String?,
        medicinePresentation: String?,
        dosage: String?) : Notification {
        // Create an Intent to open the MainActivity when the notification is clicked
        val intent = Intent(context, MainActivity::class.java).apply {
            // Set the flags to clear the task and specify the action
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = "addTakenDosage"
            // Add extras to the intent
            extras.apply {
                putExtra("idAlarm", idAlarm)
            }
        }

        // Create a pending intent for the notification action
        val flag = PendingIntent.FLAG_MUTABLE
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, idAlarm, intent, flag)

        // Define a vibration pattern for the notification
        val vibrationPattern = longArrayOf(0, 2000, 1000, 500, 500, 500)

        // // Build the notification using NotificationCompat
        val notification = NotificationCompat.Builder(context, MY_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_delete) // Set the small icon for the notification
            .setContentTitle(context.getString(R.string.time_medicine)) // Set the notification title
            .setContentText(context.getString(R.string.time_medicine_message)) // Set the notification text
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$medicineName - $dosage $medicinePresentation") // Set the expanded text
            )
            .addAction(R.drawable.medicines_icon, "DOSIS ADMINISTRADA", pendingIntent) // Add an action button
            .setVibrate(vibrationPattern) // Set the vibration pattern
            .setContentIntent(pendingIntent) // Set the content intent
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Set the priority level
            .build() // Build the notification

        return notification
    }

}