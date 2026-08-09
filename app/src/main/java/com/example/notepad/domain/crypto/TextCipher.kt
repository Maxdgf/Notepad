package com.example.notepad.domain.crypto

import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import kotlin.text.toCharArray

class TextCipher @Inject constructor() {
    private companion object {
        const val IV_GCM_LENGTH = 12 // 12 bytes required in GCM for better performance
        const val ALGORITHM = "AES"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val GCM_KEY_LENGTH = 128
    }

    /**
     * Generates PBKDF key from password chars and salt.
     *
     * @param password password chars
     * @param salt key generation salt
     *
     * @return secret key for encrypt/decode data
     * */
    private fun generatePBKDFKey(password: CharArray, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(password, salt, 65536, 256)
        val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val secretKeyTmp = secretKeyFactory.generateSecret(spec)

        return secretKeyTmp.encoded
    }


    /**
     * Encrypts text with specific password.
     *
     * @param password input password string
     * @param text text to encrypt
     *
     * @return pair of **encrypted text** and **salt**
     */
    fun encryptTextWithPassword(password: String, text: String): Pair<String, String> {
        // generate salt
        val rand = SecureRandom()
        val salt = ByteArray(32) // 256 bit length
        rand.nextBytes(salt)

        // generate secret key from password string
        val secretKey = generatePBKDFKey(password.toCharArray(), salt)
        val secretKeySpec = SecretKeySpec(secretKey, ALGORITHM)


        // generate initialization vector (IV)
        val iv = ByteArray(IV_GCM_LENGTH)
        rand.nextBytes(iv)
        val gcmSpec = GCMParameterSpec(GCM_KEY_LENGTH, iv)


        // initialize cipher object in encrypt mode
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmSpec)
        }


        // encrypt text by AES-GCM algorithm
        val encryptedContentBytes = cipher.doFinal(text.toByteArray())

        // combine IV with encrypted text
        val size = iv.size + encryptedContentBytes.size
        val encryptedContentWithIv = ByteBuffer.allocate(size)
            .put(iv)                    // put IV
            .put(encryptedContentBytes) // put encrypted content bytes
            .array()

        val base64Encoder = Base64.getEncoder() // get base64 encoder

        // encode byte arrays of data to ASCII string
        val encryptedContent = base64Encoder.encodeToString(encryptedContentWithIv) // encode encrypted text with IV
        val passwordSalt = base64Encoder.encodeToString(salt)                       // encode password's salt

        return Pair(encryptedContent, passwordSalt)
    }

    /**
     * Decrypts text with specific password.
     *
     * @param password input password string
     * @param passwordSalt password's secret key generation salt
     * @param encryptedText encrypted text content
     *
     * @return decrypted text content
     */
    fun decryptTextWithPassword(
        password: String,
        passwordSalt: String,
        encryptedText: String
    ): String {
        val base64Decoder = Base64.getDecoder() // get base64 decoder

        val salt = base64Decoder.decode(passwordSalt)
        val secretKey = generatePBKDFKey(password.toCharArray(), salt)
        val secretKeySpec = SecretKeySpec(secretKey, ALGORITHM)

        val decodedNoteContent = base64Decoder.decode(encryptedText)
        val iv = decodedNoteContent.take(IV_GCM_LENGTH).toByteArray() // extract IV from decoded text
        val gcmSpec = GCMParameterSpec(GCM_KEY_LENGTH, iv)               // create GCM parameter spec

        // initialize cipher object in decrypt mode
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, secretKeySpec, gcmSpec)
        }

        val decodedTextBytes = decodedNoteContent.sliceArray(IV_GCM_LENGTH..<decodedNoteContent.size)
        val decryptedTextBytes = cipher.doFinal(decodedTextBytes)

        return String(decryptedTextBytes) // create string object
    }
}