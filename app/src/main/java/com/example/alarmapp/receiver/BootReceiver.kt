package com.example.alarmapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.alarmapp.data.AlarmDatabase
import com.example.alarmapp.helper.AlarmManagerHelper

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val database = AlarmDatabase.getInstance(context)
            val alarmManager = AlarmManagerHelper(context)

            // Reschedule all enabled alarms
            database.getAllAlarms()
                .filter { it.isEnabled }
                .forEach { alarm ->
                    alarmManager.scheduleAlarm(alarm)
                }
        }
    }
}