package com.example.alarmapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmapp.data.AlarmDatabase
import com.example.alarmapp.helper.AlarmManagerHelper
import com.example.alarmapp.models.Alarm
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var alarmDatabase: AlarmDatabase
    private lateinit var alarmManager: AlarmManagerHelper
    private lateinit var listView: ListView
    private lateinit var nextAlarmText: TextView
    private lateinit var adapter: AlarmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmDatabase = AlarmDatabase.getInstance(this)
        alarmManager = AlarmManagerHelper(this)

        initViews()
        setupListView()
        updateUI()
    }

    private fun initViews() {
        listView = findViewById(R.id.listView)
        nextAlarmText = findViewById(R.id.nextAlarmText)

        findViewById<Button>(R.id.addAlarmButton).setOnClickListener {
            startActivity(Intent(this, AddAlarmActivity::class.java))
        }
    }

    private fun setupListView() {
        adapter = AlarmAdapter(this, mutableListOf()) { alarm, action ->
            when (action) {
                AlarmAdapter.Action.TOGGLE -> {
                    val updatedAlarm = alarm.copy(isEnabled = !alarm.isEnabled)
                    alarmDatabase.saveAlarm(updatedAlarm)
                    if (updatedAlarm.isEnabled) {
                        alarmManager.scheduleAlarm(updatedAlarm)
                    } else {
                        alarmManager.cancelAlarm(updatedAlarm.id)
                    }
                    updateUI()
                }
                AlarmAdapter.Action.DELETE -> {
                    alarmManager.cancelAlarm(alarm.id)
                    alarmDatabase.deleteAlarm(alarm.id)
                    updateUI()
                }
            }
        }
        listView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val alarms = alarmDatabase.getAllAlarms()
        adapter.updateAlarms(alarms)
        updateNextAlarmText(alarms)
    }

    private fun updateNextAlarmText(alarms: List<Alarm>) {
        val enabledAlarms = alarms.filter { it.isEnabled }
        if (enabledAlarms.isEmpty()) {
            nextAlarmText.text = "No alarms set"
            return
        }

        val nextAlarm = findNextAlarm(enabledAlarms)
        if (nextAlarm != null) {
            val timeUntil = getTimeUntilAlarm(nextAlarm)
            nextAlarmText.text = "Next alarm: ${nextAlarm.getTimeString()} ($timeUntil)"
        } else {
            nextAlarmText.text = "No upcoming alarms"
        }
    }

    private fun findNextAlarm(alarms: List<Alarm>): Alarm? {
        val now = Calendar.getInstance()
        var nextAlarm: Alarm? = null
        var shortestTime = Long.MAX_VALUE

        for (alarm in alarms) {
            val alarmTime = getNextAlarmTime(alarm)
            val timeDiff = alarmTime.timeInMillis - now.timeInMillis

            if (timeDiff in 1..<shortestTime) {
                shortestTime = timeDiff
                nextAlarm = alarm
            }
        }

        return nextAlarm
    }

    private fun getNextAlarmTime(alarm: Alarm): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, alarm.hour)
        calendar.set(Calendar.MINUTE, alarm.minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if (alarm.isOneTime) {
            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
        } else {
            // Find next occurrence based on selected days
            val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
            val today = Calendar.getInstance()

            if (calendar.timeInMillis <= today.timeInMillis || !alarm.selectedDays.contains(currentDay)) {
                // Find next selected day
                var daysToAdd = 1
                while (daysToAdd <= 7) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                    if (alarm.selectedDays.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
                        break
                    }
                    daysToAdd++
                }
            }
        }

        return calendar
    }

    private fun getTimeUntilAlarm(alarm: Alarm): String {
        val alarmTime = getNextAlarmTime(alarm)
        val now = Calendar.getInstance()
        val diff = alarmTime.timeInMillis - now.timeInMillis

        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60

        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes}m"
            else -> "Less than 1 minute"
        }
    }
}
