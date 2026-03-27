package com.example.notepad.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateTimeFormatter {
    /**
     * Formats datetime millis by pattern: `dd:MM:yyyy HH:mm:ss E`.
     * @return string datetime.
     */
    fun formatDatetimeNow(millis: Long): String {
        val instant = Instant.ofEpochMilli(millis)
        val formatter = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm:ss E")
            .withZone(ZoneId.systemDefault())

        return formatter.format(instant)
    }
}