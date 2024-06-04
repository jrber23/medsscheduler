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
    private val context: Context
) {

    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun createAlarm(alarm: Alarm) {
        val intent = Intent(context, AlarmReceiver::class.java)

        intent.putExtra("idAlarm", alarm.id)
        intent.putExtra("medicineName", alarm.medicineName)

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

        val alarmId = alarm.id.toInt()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.hourStart)
            set(Calendar.MINUTE, alarm.minuteStart)
        }

        val receiver = ComponentName(context, RebootReceiver::class.java)

        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            // viewModel.alarm.value.frequency,
            pendingIntent
        )
    }


}