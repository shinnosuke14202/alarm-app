package com.example.alarmapp.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.alarmapp.MainActivity
import com.example.alarmapp.R
import com.example.alarmapp.features.alarm.ui.DisplayAlarmActivity
import com.example.alarmapp.receiver.AlarmReceiver

class NotificationHelper {

    fun createNotificationBuilder(context: Context): NotificationCompat.Builder {
        val actionIntent = PendingIntent.getBroadcast(
            context, ALARM_RC, Intent(context, AlarmReceiver::class.java).apply {
                putExtra(ALARM_CREATE_OR_CANCEL, false)
            }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val fullScreenIntent = PendingIntent.getActivity(
            context,
            ALARM_RC,
            Intent(context, DisplayAlarmActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
            .setContentTitle("Alarm Notification").setContentText("Wake up!")
            .setSmallIcon(R.drawable.baseline_access_alarm_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(R.drawable.baseline_close_24, "Turn Off", actionIntent).setAutoCancel(true)
            .setFullScreenIntent(fullScreenIntent, true)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setLocalOnly(true).setOngoing(true)
    }

    fun createNotificationManager(context: Context): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        val channel = NotificationChannel(
            ALARM_CHANNEL_ID,
            "Alarm Notification",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "Channel for alarm"
            setSound(null, null)
            enableVibration(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        notificationManager.createNotificationChannel(channel)
        return notificationManager
    }
}