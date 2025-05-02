package com.app.fabricexercise.domain.usecase

import android.util.Log
import com.app.fabricexercise.data.repository.RegistrationRepository
import com.app.fabricexercise.util.HmacHelper
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

open class RegistrationUseCase @Inject constructor(
    private val repository: RegistrationRepository
) {

    suspend fun generateAndStoreSecretKey(): String {
        val key = HmacHelper.generateSecretKey()
        repository.saveSecretKey(key)
        return key
    }

    suspend fun storeFirebaseToken() {
        val token = FirebaseMessaging.getInstance().token.await()
        repository.saveFirebaseToken(token)
        Log.e("${javaClass.simpleName}: Registration", "Firebase Token: $token")
    }

    suspend fun verifyMessage(content: String, checksum: String): Boolean {
        val key = repository.getSecretKey()?.trim() ?: return false
       // return HmacHelper.verifyChecksum(key, content, checksum)
        return key == checksum
    }
}
