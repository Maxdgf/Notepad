package com.example.notepad.domain.crypto

import junit.framework.TestCase.assertEquals
import org.junit.Test

class TextCipherTest {
    @Test
    fun `text cipher encryption and decryption test`() {
        val text = "secret text!))))"
        val password = "AsBfhgascGtf54215!?"

        val textCipher = TextCipher()

        val encryptedText = textCipher.encryptTextWithPassword(password, text)
        val decryptedText = textCipher.decryptTextWithPassword(password, encryptedText.second, encryptedText.first)

        assertEquals(text, decryptedText)
    }
}