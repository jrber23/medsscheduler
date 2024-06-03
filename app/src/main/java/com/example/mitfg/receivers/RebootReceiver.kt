/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.receivers

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import com.example.mitfg.R
import com.example.mitfg.data.instructionAlarm.AlarmRepository
import com.example.mitfg.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * The class that contains every action to be done when the device has been rebooted
 */
@AndroidEntryPoint
class RebootReceiver : BroadcastReceiver() {

    // The alarm repository instance
    @Inject
    lateinit var alarmRepository: AlarmRepository

    /**
     * Called when the BroadcastReceiver is receiving an Intent broadcast.
     * @param context the context in which the receiver is running
     * @param intent the intent being received
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        // Check if the received intent action is BOOT_COMPLETED
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            // Create a notification channel
            val channel = NotificationChannel(
                MainActivity.MY_NOTIFICATION_CHANNEL_ID,
                "MySuperChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "SUSCRIBETE"
            }

            // Get the NotificationManager system service
            val notificationManager: NotificationManager =
                context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            // Get the AlarmManager system service
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // Intent to trigger the AlarmReceiver
            val intentAlarm = Intent(context, AlarmReceiver::class.java)
            var haveBeenExecuted = false

            // Launch a coroutine to fetch all alarms and reschedule them
            CoroutineScope(Dispatchers.IO).launch {
                alarmRepository.getAllAlarms().collect { receivedList ->
                    if (!haveBeenExecuted) {
                        Log.d("REBOOT_RECEIVER", receivedList.size.toString())
                        for (alarm in receivedList) {
                            // Set alarm details in the intent
                            intentAlarm.putExtra("idAlarm", alarm.id)
                            intentAlarm.putExtra("medicineName", alarm.medicineName)

                            // Set the alarm time
                            val calendar: Calendar = Calendar.getInstance().apply {
                                timeInMillis = System.currentTimeMillis()
                                set(Calendar.HOUR_OF_DAY, alarm.hourStart)
                                set(Calendar.MINUTE, alarm.minuteStart)
                            }

                            // Determine the medicine presentation
                            val medicinePresentation = when (alarm.medicinePresentation) {
                                context.getString(R.string.pillAbrev) -> context.getString(
                                    R.string.showPill)
                                context.getString(R.string.packetAbrev) -> context.getString(
                                    R.string.showPacket)
                                context.getString(R.string.MlAbrev) -> context.getString(
                                    R.string.showMl)
                                else -> ""
                            }

                            intentAlarm.putExtra("medicinePresentation", medicinePresentation)
                            intentAlarm.putExtra("dosage", alarm.quantity)

                            // Create a pending intent for the alarm
                            val pendingIntent = PendingIntent.getBroadcast(
                                context,
                                alarm.id.toInt(),
                                intentAlarm,
                                PendingIntent.FLAG_MUTABLE
                            )

                            // Set the alarm with AlarmManager
                            alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis,
                                // alarm.frequency
                                pendingIntent
                            )
                        }
                        haveBeenExecuted = true
                    }
                }
            }
        }
    }
}