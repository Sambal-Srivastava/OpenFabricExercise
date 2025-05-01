package com.app.fabricexercise.ui.registration

sealed class RegistrationState {
    object Initial : RegistrationState()
    object InProgress : RegistrationState()
    object Registered : RegistrationState()
    object RegistrationFailed : RegistrationState()
    object RegistrationExpired : RegistrationState()
}
