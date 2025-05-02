package com.app.fabricexercise.util

import android.util.Base64
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HmacHelperTest {

    @Test
    fun generateSecretKey_returnsValidBase64EncodedKey() {
        val base64Key = HmacHelper.generateSecretKey()

        assertNotNull("Key should not be null", base64Key)
        assertTrue("Key should not be blank", base64Key.isNotBlank())

        // Decode the Base64 key and check the length
        val decodedKey = Base64.decode(base64Key, Base64.DEFAULT)
        assertEquals("Decoded key should be 32 bytes", 32, decodedKey.size)
    }
}
