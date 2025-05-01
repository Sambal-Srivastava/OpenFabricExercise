package com.app.fabricexercise.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object RegistrationEventBus {
    private val _registrationResult = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    val registrationResult: SharedFlow<Boolean> = _registrationResult

    fun publishResult(isValid: Boolean) {
        _registrationResult.tryEmit(isValid)
    }
}
