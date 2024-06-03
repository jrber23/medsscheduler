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

@AndroidEntryPoint
class RebootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmRepository: AlarmRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            val channel = NotificationChannel(
                MainActivity.MY_NOTIFICATION_CHANNEL_ID,
                "MySuperChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "SUSCRIBETE"
            }

            val notificationManager: NotificationManager =
                context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(context, AlarmReceiver::class.java)

            var haveBeenExecuted = false

            CoroutineScope(Dispatchers.IO).launch {
                    alarmRepository.getAllAlarms().collect { receivedList ->
                        if (!haveBeenExecuted) {
                            Log.d("REBOOT_RECEIVER", receivedList.size.toString())
                            for (alarm in receivedList) {
                                intent.putExtra("idAlarm", alarm.id)
                                intent.putExtra("medicineName", alarm.medicineName)

                                val calendar: Calendar = Calendar.getInstance().apply {
                                    timeInMillis = System.currentTimeMillis()
                                    set(Calendar.HOUR_OF_DAY, alarm.hourStart)
                                    set(Calendar.MINUTE, alarm.minuteStart)
                                }

                                var medicinePresentation = ""

                                when (alarm.medicinePresentation) {
                                    context.getString(R.string.pillAbrev) -> medicinePresentation = context.getString(
                                        R.string.showPill)
                                    context.getString(R.string.packetAbrev) -> medicinePresentation = context.getString(
                                        R.string.showPacket)
                                    context.getString(R.string.MlAbrev) -> medicinePresentation = context.getString(
                                        R.string.showMl)
                                    else -> ""
                                }

                                intent.putExtra("medicinePresentation", medicinePresentation)
                                intent.putExtra("dosage", alarm.quantity)

                                val pendingIntent = PendingIntent.getBroadcast(
                                    context,
                                    alarm.id.toInt(),
                                    intent,
                                    PendingIntent.FLAG_MUTABLE
                                )

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