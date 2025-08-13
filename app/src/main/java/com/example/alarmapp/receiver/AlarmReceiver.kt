package com.example.alarmapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.alarmapp.AlarmRingActivity
import com.example.alarmapp.data.AlarmDatabase
import com.example.alarmapp.AlarmService
import com.example.alarmapp.helper.AlarmManagerHelper
import com.example.alarmapp.models.Alarm

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarm = intent.getParcelableExtra<Alarm>("alarm")

        if (alarm != null) {
            // Start the alarm service
            val serviceIntent = Intent(context, AlarmService::class.java).apply {
                putExtra("alarm", alarm)
            }
            context.startForegroundService(serviceIntent)

            // Show alarm activity
            val alarmIntent = Intent(context, AlarmRingActivity::class.java).apply {
                putExtra("alarm", alarm)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(alarmIntent)

            // Reschedule if it's a recurring alarm
            if (!alarm.isOneTime) {
                val alarmManager = AlarmManagerHelper(context)
                alarmManager.scheduleAlarm(alarm)
            } else {
                // Disable one-time alarm
                val database = AlarmDatabase.getInstance(context)
                database.saveAlarm(alarm.copy(isEnabled = false))
            }
        }
    }
}