package com.example.alarmapp.features.alarm.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmapp.databinding.ActivityDisplayAlarmBinding
import com.example.alarmapp.features.alarm.service.AlarmService
import java.time.LocalDateTime

class DisplayAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDisplayAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val hour = LocalDateTime.now().hour
        val minute = LocalDateTime.now().minute

        binding.tvHour.text = String.format("%02d", hour)
        binding.tvMinute.text = String.format("%02d", minute)

        binding.btnDismiss.setOnClickListener {
            // Send signal to stop alarm service
            val stopIntent = Intent(this, AlarmService::class.java).apply {
                action = "STOP_ALARM"
            }
            startService(stopIntent)

            // Finish this full-screen activity
            finish()
        }

        // Allows activity to show when device is locked
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }

    }
}