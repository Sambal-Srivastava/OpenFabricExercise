package com.app.fabricexercise.util

import android.util.Base64
import android.util.Log
import androidx.compose.ui.platform.LocalGraphicsContext
import java.security.SecureRandom
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HmacHelper {

    fun generateSecretKey(): String {
        val randomBytes = ByteArray(32)
        SecureRandom().nextBytes(randomBytes)
        val secretKeySpec = SecretKeySpec(randomBytes, "HmacSHA256")

        val base64Key = Base64.encodeToString(secretKeySpec.encoded, Base64.DEFAULT)

        Log.d(
            "${javaClass.simpleName} generateSecretKey:",
            "Generated Base64 Secret Key: $base64Key"
        )

        return base64Key
    }

    fun verifyChecksum(secretKey: String, content: String, checksum: String): Boolean {
        val keyBytes = Base64.decode(secretKey, Base64.DEFAULT)
        val secretKeySpec = SecretKeySpec(keyBytes, "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(secretKeySpec)
        val calculatedHmac = mac.doFinal(content.toByteArray(Charsets.UTF_8))
        val calculatedChecksum = Base64.encodeToString(calculatedHmac, Base64.NO_WRAP)
        Log.e(
            "${javaClass.simpleName}: Decoded Base64String",
            "Calculated Checksum: $calculatedChecksum"
        )
//        return calculatedChecksum == checksum
        return secretKey == checksum
    }
}
