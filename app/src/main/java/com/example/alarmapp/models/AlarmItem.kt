package com.example.alarmapp.models

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val message: String,
)
