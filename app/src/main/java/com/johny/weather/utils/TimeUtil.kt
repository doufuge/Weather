package com.johny.weather.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimeUtil {

    companion object {
        private val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        private val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        private val outputHourFormatter = DateTimeFormatter.ofPattern("HH")

        fun format(timeStr: String): String {
            LocalDateTime.parse(timeStr, inputFormatter).let {
                return it.format(outputFormatter)
            }
        }

        fun formatHour(timeStr: String): String {
            LocalDateTime.parse(timeStr, inputFormatter).let {
                return it.format(outputHourFormatter)
            }
        }
    }

}