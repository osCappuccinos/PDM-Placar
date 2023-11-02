package com.pdm.placar

import android.os.Handler
import android.os.Looper

class TimerManager(private val updateCallback: () -> Unit) {
    private val handler = Handler(Looper.getMainLooper())
    private var seconds = 0
    private var isTimerRunning = false
    private var timerRunnable: Runnable? = null
    private var timeLimit = 30 //2700
    private var extraTime = 0
    private var isSecondHalf = false

    fun startTimer() {
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
                            extraTime = 0
                            stopTimer()
                        } else {
                            stopTimer()
                        }
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

    fun resetTimer() {
        isTimerRunning = false
        timerRunnable?.let {
            handler.removeCallbacks(it)
        }
        timerRunnable = null
        seconds = 0
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

    companion object {
        const val TIMER_KEY = "TIMER_VALUE"
        const val TIMER_RUNNING_KEY = "TIMER_RUNNING"
    }
}
