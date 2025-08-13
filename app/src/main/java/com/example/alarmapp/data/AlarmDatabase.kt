package com.example.alarmapp.data

import android.content.Context
import android.content.SharedPreferences
import com.example.alarmapp.models.Alarm
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlarmDatabase private constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("alarms", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        @Volatile
        private var INSTANCE: AlarmDatabase? = null

        fun getInstance(context: Context): AlarmDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AlarmDatabase(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun saveAlarm(alarm: Alarm) {
        val alarms = getAllAlarms().toMutableList()
        val existingIndex = alarms.indexOfFirst { it.id == alarm.id }

        if (existingIndex != -1) {
            alarms[existingIndex] = alarm
        } else {
            alarms.add(alarm)
        }

        saveAlarms(alarms)
    }

    fun deleteAlarm(alarmId: Long) {
        val alarms = getAllAlarms().filter { it.id != alarmId }
        saveAlarms(alarms)
    }

    fun getAllAlarms(): List<Alarm> {
        val alarmsJson = prefs.getString("alarms_list", "[]")
        val type = object : TypeToken<List<Alarm>>() {}.type
        return gson.fromJson(alarmsJson, type) ?: emptyList()
    }

    private fun saveAlarms(alarms: List<Alarm>) {
        prefs.edit().putString("alarms_list", gson.toJson(alarms)).apply()
    }
}