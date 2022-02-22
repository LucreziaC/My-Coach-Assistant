package com.lucreziacarena.mycoachassistant.views.sessionScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lucreziacarena.mycoachassistant.utils.Utils.Companion.formatTime
import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class StopWatch() {
    var formattedTime by mutableStateOf("00:00:000")
    var time by mutableStateOf(0L)
    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private var isActive = false

    private var timeMillis = 0L
    private var lastTimeStamp = 0L

    @RequiresApi(Build.VERSION_CODES.O)
    fun start() {
        if (isActive) return
        coroutineScope.launch {
            lastTimeStamp = System.currentTimeMillis()
            this@StopWatch.isActive = true
            while (isActive) {
                delay(10L) //every 10 millisecond update ui
                timeMillis += System.currentTimeMillis() - lastTimeStamp
                lastTimeStamp = System.currentTimeMillis()
                time =timeMillis
                formattedTime = formatTime(timeMillis)
            }
        }
    }

    fun getTimOnLap(): Long = time

    fun stop() {
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        timeMillis = 0L
        lastTimeStamp = 0L
        formattedTime= "00:00:000"
        isActive = false
    }


}
