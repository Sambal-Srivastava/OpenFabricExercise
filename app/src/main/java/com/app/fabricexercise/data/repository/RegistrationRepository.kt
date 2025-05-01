package com.app.fabricexercise.data.repository

interface RegistrationRepository {
    suspend fun saveSecretKey(key: String)
    suspend fun getSecretKey(): String?

    suspend fun saveFirebaseToken(token: String)
    suspend fun getFirebaseToken(): String?
}
