package com.example.e_paper.classes

import android.os.CountDownTimer
import android.os.Handler
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Timer {
    var isRunning by mutableStateOf(false)
    var currentTimeInMillis by mutableStateOf(0L)
    var currentTimeInSecond by mutableStateOf(0L)

    private val handler = Handler()
    private lateinit var runnable: Runnable

    fun start() {
        isRunning = true
        runnable = object : Runnable {
            override fun run() {
                currentTimeInMillis += 1000
                currentTimeInSecond = currentTimeInMillis / 1000
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(runnable, 1000)
    }

    fun stop() {
        isRunning = false
        handler.removeCallbacks(runnable)
    }

    fun reset() {
        currentTimeInMillis = 0L
        isRunning = false
        handler.removeCallbacks(runnable)
    }
}