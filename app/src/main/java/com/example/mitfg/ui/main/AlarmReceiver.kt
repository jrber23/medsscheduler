package com.example.mitfg.ui.main

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.mitfg.R
import com.example.mitfg.ui.newAlarm.alarmCreationStages.FrequencyFragment

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context, intent: Intent) {
        val idAlarm = intent.getIntExtra("idAlarm", -1)
        val medicineName = intent.getStringExtra("medicineName")
        val medicinePresentation = intent.getStringExtra("medicinePresentation")
        val dosage = intent.getStringExtra("dosage")

        createSimpleNotification(context, idAlarm, medicineName, medicinePresentation, dosage)
    }

    private fun createSimpleNotification(
        context: Context,
        idAlarm: Int,
        medicineName: String?,
        medicinePresentation: String?,
        dosage: String?
    ) {
        val intent = Intent(context, FrequencyFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = PendingIntent.FLAG_MUTABLE
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, idAlarm.toInt(), intent, flag)
        val vibrationPattern = longArrayOf(0, 2000, 1000, 500, 500, 500)

        val notification = NotificationCompat.Builder(context, MainActivity.MY_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_delete)
            .setContentTitle(context.getString(R.string.time_medicine))
            .setContentText(context.getString(R.string.time_medicine_message))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$medicineName - $dosage $medicinePresentation")
            )
            .setVibrate(vibrationPattern)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(idAlarm, notification)
    }
}