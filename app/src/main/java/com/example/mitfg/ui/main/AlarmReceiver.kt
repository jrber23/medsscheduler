package com.example.mitfg.ui.main

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context, intent: Intent) {
        val idAlarm = intent.getLongExtra("idAlarm", -1)
        val medicineName = intent.getStringExtra("medicineName")
        val medicinePresentation = intent.getStringExtra("medicinePresentation")
        val dosage = intent.getIntExtra("dosage", -1)

        createSimpleNotification(context, idAlarm, medicineName, medicinePresentation, dosage)
    }

    private fun createSimpleNotification(
        context: Context,
        idAlarm: Long,
        medicineName: String?,
        medicinePresentation: String?,
        dosage: Int?
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, idAlarm.toInt(), intent, flag)
        val vibrationPattern = longArrayOf(0, 2000, 1000, 500, 500, 500)

        val notification = NotificationCompat.Builder(context, MainActivity.MY_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_delete)
            .setContentTitle("Hora de la medicina!!")
            .setContentText("Ha llegado el momento de que te tomes la dosis correspondiente")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$medicineName - $dosage $medicinePresentation")
            )
            .setVibrate(vibrationPattern)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(idAlarm.toInt(), notification)
    }
}