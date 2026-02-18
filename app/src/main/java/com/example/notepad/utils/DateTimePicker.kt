package com.example.notepad.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimePicker {
    /**
     * Picks datetime by pattern: `dd:MM:yyyy HH:mm:ss E`.
     * @return string datetime.
     */
    fun pickDateTimeNow(): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm:ss E")
        val currentDateTime: String = LocalDateTime.now().format(dateTimeFormatter)

        return currentDateTime
    }
}