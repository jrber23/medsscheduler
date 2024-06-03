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

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmRepository: AlarmRepository

    override fun onReceive(context: Context, intent: Intent) {
        val idAlarm = intent.getLongExtra("idAlarm", -1)
        val medicineName = intent.getStringExtra("medicineName")
        val medicinePresentation = intent.getStringExtra("medicinePresentation")
        val dosage = intent.getStringExtra("dosage")

        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.addDosageToAlarm(idAlarm)
        }

        createSimpleNotification(context, idAlarm, medicineName, medicinePresentation, dosage)
    }

    private fun createSimpleNotification(
        context: Context,
        idAlarm: Long,
        medicineName: String?,
        medicinePresentation: String?,
        dosage: String?
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            /* extras.apply {
                putExtra("alarmDialog", true)
            } */

            action = "addTakenDosage"
            extras.apply {
                putExtra("idAlarm", idAlarm)
            }
        }



        val flag = PendingIntent.FLAG_MUTABLE
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, idAlarm.toInt(), intent, flag)
        val vibrationPattern = longArrayOf(0, 2000, 1000, 500, 500, 500)

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

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(idAlarm.toInt(), notification)
    }
}