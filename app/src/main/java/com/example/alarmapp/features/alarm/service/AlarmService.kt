package com.example.alarmapp.features.alarm.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import com.example.alarmapp.utils.NotificationHelper

class AlarmService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val notificationHelper = NotificationHelper()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP_ALARM") {
            stopSelf()
            return START_NOT_STICKY
        }

        val notificationBuilder = notificationHelper.createNotificationBuilder(this)
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mediaPlayer = MediaPlayer().apply {
            setDataSource(this@AlarmService, alarmUri)
            isLooping = true
            setOnPreparedListener { it.start() }
            prepareAsync()
        }

        if (vibrator.hasVibrator()) {
            val pattern = longArrayOf(0, 500, 1000) // Wait 0ms, vibrate 500ms, sleep 1000ms
            val effect = VibrationEffect.createWaveform(pattern, 0) // 0 = repeat from index 0
            vibrator.vibrate(effect)
        }

        // Foreground notification to keep service alive
        val notification = notificationBuilder.build()

        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}