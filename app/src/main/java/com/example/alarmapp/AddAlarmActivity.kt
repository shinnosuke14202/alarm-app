package com.example.alarmapp

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Switch
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmapp.data.AlarmDatabase
import com.example.alarmapp.helper.AlarmManagerHelper
import com.example.alarmapp.models.Alarm

class AddAlarmActivity : AppCompatActivity() {
    private lateinit var timePicker: TimePicker
    private lateinit var labelEdit: EditText
    private lateinit var dayCheckboxes: Map<Int, CheckBox>
    private lateinit var soundSwitch: Switch
    private lateinit var vibrationSwitch: Switch
    private lateinit var alarmDatabase: AlarmDatabase
    private lateinit var alarmManager: AlarmManagerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)

        alarmDatabase = AlarmDatabase.getInstance(this)
        alarmManager = AlarmManagerHelper(this)

        initViews()
        setupSaveButton()
    }

    private fun initViews() {
        timePicker = findViewById(R.id.timePicker)
        labelEdit = findViewById(R.id.labelEdit)
        soundSwitch = findViewById(R.id.soundSwitch)
        vibrationSwitch = findViewById(R.id.vibrationSwitch)

        dayCheckboxes = mapOf(
            1 to findViewById(R.id.sundayCheckbox),
            2 to findViewById(R.id.mondayCheckbox),
            3 to findViewById(R.id.tuesdayCheckbox),
            4 to findViewById(R.id.wednesdayCheckbox),
            5 to findViewById(R.id.thursdayCheckbox),
            6 to findViewById(R.id.fridayCheckbox),
            7 to findViewById(R.id.saturdayCheckbox)
        )

        // Set defaults
        soundSwitch.isChecked = true
        vibrationSwitch.isChecked = true
    }

    private fun setupSaveButton() {
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            saveAlarm()
        }

        findViewById<Button>(R.id.cancelButton).setOnClickListener {
            finish()
        }
    }

    private fun saveAlarm() {
        val selectedDays = dayCheckboxes.filter { it.value.isChecked }.keys

        val alarm = Alarm(
            hour = timePicker.hour,
            minute = timePicker.minute,
            label = labelEdit.text.toString(),
            selectedDays = selectedDays,
            soundEnabled = soundSwitch.isChecked,
            vibrationEnabled = vibrationSwitch.isChecked
        )

        alarmDatabase.saveAlarm(alarm)
        alarmManager.scheduleAlarm(alarm)

        Toast.makeText(this, "Alarm saved", Toast.LENGTH_SHORT).show()
        finish()
    }
}