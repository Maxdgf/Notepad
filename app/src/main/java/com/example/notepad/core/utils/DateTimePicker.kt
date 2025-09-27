package com.example.notepad.core.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimePicker {
    fun pickDateTimeNow(): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm:ss E")
        val currentDateTime: String = LocalDateTime.now().format(dateTimeFormatter)

        return currentDateTime
    }
}