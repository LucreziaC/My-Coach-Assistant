package com.lucreziacarena.mycoachassistant.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class Utils {
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun formatTime(timeMillis: Long): String {
            val localDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timeMillis),
                ZoneId.systemDefault()
            )
            val formatter = DateTimeFormatter.ofPattern("mm:ss:SSS", Locale.getDefault())
            return localDateTime.format(formatter)
        }
    }
}
