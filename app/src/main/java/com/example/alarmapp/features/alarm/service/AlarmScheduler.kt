package com.example.alarmapp.features.alarm.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.alarmapp.models.AlarmItem
import com.example.alarmapp.receiver.AlarmReceiver
import com.example.alarmapp.utils.ALARM_CREATE_OR_CANCEL
import java.time.ZoneId

class AlarmScheduler(private val context: Context) {

    private val alarmScheduler = context.getSystemService(AlarmManager::class.java)

    fun schedule(item: AlarmItem) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmScheduler.canScheduleExactAlarms()) {
                setExactAlarm(item)
            }
        } else {
            setExactAlarm(item)
        }
    }

    private fun setExactAlarm(item: AlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(ALARM_CREATE_OR_CANCEL, true)
        }
        alarmScheduler.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}