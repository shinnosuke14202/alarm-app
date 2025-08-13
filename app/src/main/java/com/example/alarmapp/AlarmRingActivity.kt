package com.example.alarmapp

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmapp.helper.AlarmManagerHelper
import com.example.alarmapp.models.Alarm

class AlarmRingActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_ring)
        
        // Show over lock screen
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
        
        val alarm = intent.getParcelableExtra<Alarm>("alarm")
        
        findViewById<TextView>(R.id.alarmTimeText).text = alarm?.getTimeString() ?: ""
        findViewById<TextView>(R.id.alarmLabelText).text = alarm?.label ?: "Alarm"
        
        findViewById<Button>(R.id.dismissButton).setOnClickListener {
            dismissAlarm()
        }
        
        findViewById<Button>(R.id.snoozeButton).setOnClickListener {
            snoozeAlarm(alarm)
        }
    }
    
    private fun dismissAlarm() {
        stopService(Intent(this, AlarmService::class.java))
        finish()
    }
    
    private fun snoozeAlarm(alarm: Alarm?) {
        if (alarm != null) {
            // Create a snooze alarm for 5 minutes later
            val snoozeAlarm = alarm.copy(
                id = System.currentTimeMillis(),
                hour = ((System.currentTimeMillis() / (1000 * 60 * 60)) % 24).toInt(),
                minute = (((System.currentTimeMillis() / (1000 * 60)) % 60 + 5) % 60).toInt(),
                selectedDays = emptySet() // Make it one-time
            )
            
            val alarmManager = AlarmManagerHelper(this)
            alarmManager.scheduleAlarm(snoozeAlarm)
        }
        
        dismissAlarm()
    }
}