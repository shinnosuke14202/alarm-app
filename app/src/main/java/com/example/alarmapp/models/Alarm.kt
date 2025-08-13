package com.example.alarmapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class Alarm(
    val id: Long = System.currentTimeMillis(),
    val hour: Int,
    val minute: Int,
    val label: String = "",
    val isEnabled: Boolean = true,
    val selectedDays: Set<Int> = emptySet(), // 1=Sunday, 2=Monday, etc.
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val isOneTime: Boolean = selectedDays.isEmpty()
) : Parcelable {

    fun getTimeString(): String {
        val amPm = if (hour >= 12) "PM" else "AM"
        val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        return String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, minute, amPm)
    }

    fun getDaysString(): String {
        if (isOneTime) return "One time"
        if (selectedDays.size == 7) return "Every day"

        val dayNames = mapOf(
            1 to "Sun", 2 to "Mon", 3 to "Tue", 4 to "Wed", 5 to "Thu", 6 to "Fri", 7 to "Sat"
        )

        return selectedDays.sorted().joinToString(", ") { dayNames[it] ?: "" }
    }
}