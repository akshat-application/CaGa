package com.dog.truefrienddog.utils

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Aes {

    private const val AES_ALGORITHM = "AES"
    private const val ENCRYPTION_KEY = "wrt_add09rggh-ttyw6rRvdiopxmEioc"
    private const val ENCRYPTION_IV = "hkl_opku9r9UK-po"

    private const val old_encryption = "f@a_ddarbr9UK-gggw6rTUjyklkoW0iT"
    private const val encryption_iv = "fCa_BRPpol9UK-UP"

    fun encryptObject(obj: String): String {
        val encryptedBytes = encrypt(obj.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    fun decryptObject(encryptedString: String): String {
        val encryptedBytes = Base64.getDecoder().decode(encryptedString)
        val decryptedBytes = decrypt(encryptedBytes)
        return String(decryptedBytes, StandardCharsets.UTF_8)
    }

    private fun encrypt(data: ByteArray): ByteArray {
        val secretKey = SecretKeySpec(ENCRYPTION_KEY.toByteArray(), AES_ALGORITHM)
        val ivSpec = IvParameterSpec(ENCRYPTION_IV.toByteArray())
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        return cipher.doFinal(data)
    }

    private fun decrypt(data: ByteArray): ByteArray {
        val secretKey = SecretKeySpec(ENCRYPTION_KEY.toByteArray(), AES_ALGORITHM)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(ENCRYPTION_IV.toByteArray())
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        return cipher.doFinal(data)
    }
}