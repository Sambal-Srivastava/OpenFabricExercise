package com.app.fabricexercise.ui.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.fabricexercise.domain.model.MessagePayload
import com.app.fabricexercise.domain.usecase.RegistrationUseCase
import com.app.fabricexercise.util.RegistrationEventBus
import com.app.fabricexercise.util.TimerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
open class RegistrationViewModel @Inject constructor(
    private val useCase: RegistrationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<RegistrationState>(RegistrationState.Initial)
    open val state: StateFlow<RegistrationState> = _state

    private val timerHelper = TimerHelper()

    open val remainingFormattedTime: StateFlow<String> = timerHelper.remainingTime
        .map { seconds ->
            val minutes = seconds / 60
            val secs = seconds % 60
            String.format("%02d:%02d", minutes, secs)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "15:00")

    init {
        observeRegistrationResult()
    }

    private fun observeRegistrationResult() {
        viewModelScope.launch {
            RegistrationEventBus.registrationResult.collect { isValid ->
                if (_state.value == RegistrationState.InProgress) {
                    timerHelper.cancelTimer()
                    _state.value =
                        if (isValid) RegistrationState.Registered else RegistrationState.RegistrationFailed
                    delay(10000)
                    _state.value = RegistrationState.Initial
                }
            }
        }
    }

    open fun registerDevice() {
        viewModelScope.launch {
            val secretKey = useCase.generateAndStoreSecretKey()
            Log.e("${javaClass.simpleName}: Registration", "Generated Secret Key: $secretKey")
            _state.value = RegistrationState.InProgress
            useCase.storeFirebaseToken()
            timerHelper.startCountdown(viewModelScope) {
                _state.value = RegistrationState.RegistrationExpired
                viewModelScope.launch {
                    delay(10000)
                    _state.value = RegistrationState.Initial
                }
            }
        }
    }

    fun resetRegistration() {
        _state.value = RegistrationState.Initial
    }
}
