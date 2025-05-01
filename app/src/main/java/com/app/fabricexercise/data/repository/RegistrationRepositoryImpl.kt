package com.app.fabricexercise.data.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import javax.inject.Inject

class RegistrationRepositoryImpl @Inject constructor(
    private val context: Context
) : RegistrationRepository {

    private val sharedPrefs = EncryptedSharedPreferences.create(
        "secure_prefs",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override suspend fun saveSecretKey(key: String) {
        sharedPrefs.edit().putString("hmac_secret_key", key).apply()
    }

    override suspend fun getSecretKey(): String? {
        return sharedPrefs.getString("hmac_secret_key", null)
    }

    override suspend fun saveFirebaseToken(token: String) {
        sharedPrefs.edit().putString("firebase_token", token).apply()
    }

    override suspend fun getFirebaseToken(): String? {
        return sharedPrefs.getString("firebase_token", null)
    }
}
