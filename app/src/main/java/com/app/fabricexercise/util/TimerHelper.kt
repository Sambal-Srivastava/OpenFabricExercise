package com.app.fabricexercise.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimerHelper {
    private val _remainingTime = MutableStateFlow(15 * 60) // 15 minutes in seconds
    val remainingTime: StateFlow<Int> = _remainingTime

    private var job: Job? = null

    fun startTimer(scope: CoroutineScope, onTimeout: () -> Unit) {
        job = scope.launch {
            delay(15 * 1 * 1000L) // 15 minutes
            onTimeout()
        }
    }

    fun startCountdown(scope: CoroutineScope, onTimeout: () -> Unit) {
        cancelTimer() // in case already running

        job = scope.launch {
            for (time in 15 * 60 downTo 0) {
                _remainingTime.value = time
                delay(1000L)
            }
            onTimeout()
        }
    }

    fun cancelTimer() {
        job?.cancel()
    }
}
