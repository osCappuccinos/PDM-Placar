package com.pdm.placar

import android.os.Handler
import android.os.Looper

object TimerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var seconds = 0
    private var isTimerRunning = false
    private var timerRunnable: Runnable? = null
    private var timeLimit = 2700 //45 min
    private var extraTime = 0
    private var isSecondHalf = false

    fun startTimer(updateCallback: () -> Unit) {
        if (seconds < timeLimit + extraTime) {
            isTimerRunning = true
            timerRunnable = object : Runnable {
                override fun run() {
                    seconds++
                    updateCallback()
                    if (seconds >= timeLimit + extraTime) {
                        if (!isSecondHalf) {
                            isSecondHalf = true
                            timeLimit *= 2
                            seconds -= extraTime
                            extraTime = 0
                        }
                        stopTimer()
                    } else {
                        handler.postDelayed(this, 1000)
                    }
                }
            }
            timerRunnable?.let { handler.post(it) }
        }
    }

    fun stopTimer() {
        isTimerRunning = false
        timerRunnable?.let {
            handler.removeCallbacks(it)
        }
        timerRunnable = null
    }

    fun resetTimer(updateCallback: () -> Unit) {
        isTimerRunning = false
        timerRunnable?.let {
            handler.removeCallbacks(it)
        }
        timerRunnable = null
        seconds = 0
        if (isSecondHalf) timeLimit /= 2
        isSecondHalf = false
        updateCallback()
    }

    fun getTimerValue(): Int {
        return seconds
    }

    fun isTimerRunning(): Boolean {
        return isTimerRunning
    }

    fun isSecondHalf(): Boolean {
        return isSecondHalf
    }

    fun restoreTimerState(savedSeconds: Int, savedIsTimerRunning: Boolean) {
        seconds = savedSeconds
        isTimerRunning = savedIsTimerRunning
    }

    fun setupExtraTime(seconds: Int) {
        extraTime = seconds
    }
}
