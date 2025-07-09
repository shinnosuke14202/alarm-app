package com.example.alarmapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.alarmapp.features.alarm.service.AlarmService
import com.example.alarmapp.utils.ALARM_CREATE_OR_CANCEL

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val chooseCreate = intent?.getBooleanExtra(ALARM_CREATE_OR_CANCEL, false) ?: return

        if (chooseCreate) {
            val serviceIntent = Intent(context, AlarmService::class.java)
            ContextCompat.startForegroundService(context!!, serviceIntent)
            Toast.makeText(context, "Start Service!", Toast.LENGTH_SHORT).show()
        } else {
            val stopIntent = Intent(context, AlarmService::class.java)
            stopIntent.action = "STOP_ALARM"
            context?.startService(stopIntent)
        }
    }
}