package com.app.fabricexercise.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.fabricexercise.R
import com.app.fabricexercise.domain.model.MessagePayload
import com.app.fabricexercise.domain.usecase.RegistrationUseCase
import com.app.fabricexercise.ui.registration.RegistrationViewModel
import com.app.fabricexercise.util.RegistrationEventBus
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFirebaseService : FirebaseMessagingService() {

    @Inject
    lateinit var registrationUseCase: RegistrationUseCase

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.data.let { data ->
            val content = data["content"] ?: return
            val checksum = data["checksum"] ?: return

            if (content != null && checksum != null) {
                showNotification(content)
                CoroutineScope(Dispatchers.IO).launch {
                    val isValid = registrationUseCase.verifyMessage(content, checksum.trim())
                    RegistrationEventBus.publishResult(isValid)
                }
            }
        }
    }


    private fun showNotification(content: String) {
        val channelId = "fcm_channel_id"
        val channelName = "FCM Notifications"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // For Android 8+ (Oreo), create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for FCM messages"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Verification Message")
            .setContentText(content)
            .setSmallIcon(R.drawable.open_fabric_logo) // Use your app's icon or a notification icon
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

}
