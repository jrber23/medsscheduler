/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: José Ramón Bermejo Canet
 *                jrber222@gmail.com
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package com.example.mitfg.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.mitfg.R
import com.example.mitfg.domain.model.Alarm
import com.example.mitfg.receivers.AlarmReceiver
import com.example.mitfg.receivers.RebootReceiver
import java.util.Calendar
import javax.inject.Inject

class AlarmManagerHelper @Inject constructor(
    val context: Context
) {

    // The AlarmManager instance for scheduling alarms
    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /**
     * Creates an alarm with the specified parameters from the Alarm object
     * @param alarm The alarm that contains the data to set in the AlarmManager object
     */
    fun createAlarm(alarm: Alarm) {
        // Create an Intent to broadcast when the alarm goes off
        val intent = Intent(context, AlarmReceiver::class.java)

        intent.putExtra("idAlarm", alarm.id)
        intent.putExtra("medicineName", alarm.medicineName)

        // Determine the presentation of the medicine
        val medicinePresentation = when (alarm.medicinePresentation) {
            context.getString(R.string.pillAbrev) -> context.getString(
                R.string.showPill)
            context.getString(R.string.packetAbrev) -> context.getString(
                R.string.showPacket)
            context.getString(R.string.MlAbrev) -> context.getString(
                R.string.showMl)
            else -> ""
        }

        intent.putExtra("medicinePresentation", medicinePresentation)
        intent.putExtra("dosage", alarm.quantity)

        // Create a PendingIntent to be triggered when the alarm goes off
        val alarmId = alarm.id.toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        // Set the calendar time for the alarm
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.hourStart)
            set(Calendar.MINUTE, alarm.minuteStart)
        }

        // Enable the RebootReceiver to ensure alarms are reset on reboot
        val receiver = ComponentName(context, RebootReceiver::class.java)
        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        // Set the alarm using AlarmManager
        /* alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarm.frequency,
            pendingIntent
        ) */
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            // viewModel.alarm.value.frequency,
            pendingIntent
        )
    }

    /**
     * Cancels the alarm with the given ID.
     *
     * @param alarmId The ID of the alarm to cancel.
     */
    fun cancelAlarm(alarmId : Int) {
        // Create an Intent to cancel the alarm
        val intent = Intent(context, AlarmReceiver::class.java)

        // Create a PendingIntent for the alarm to be canceled
        val alarmToCancel : PendingIntent =
            PendingIntent.getBroadcast(
                context,
                alarmId,
                intent,
                PendingIntent.FLAG_MUTABLE
            )

        // Cancel the alarm using AlarmManager
        alarmManager.cancel(alarmToCancel)
    }


}