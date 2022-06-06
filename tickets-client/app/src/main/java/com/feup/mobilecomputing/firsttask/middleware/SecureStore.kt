package com.feup.mobilecomputing.firsttask.middleware

import android.content.Context
import android.security.KeyPairGeneratorSpec
import android.util.Base64
import android.util.Log
import com.feup.mobilecomputing.firsttask.config.Config
import java.math.BigInteger
import java.security.*
import java.util.*
import javax.crypto.Cipher
import javax.security.auth.x500.X500Principal

class SecureStore {

    private val keyStore: KeyStore = KeyStore.getInstance(Config.ANDROID_KEYSTORE).apply {
        load(null)
        getEntry(Config.keyname, null)
    }

    private val entry = keyStore.getEntry(Config.keyname, null)

    fun getPubKey(): PublicKey {
        return (entry as KeyStore.PrivateKeyEntry).certificate.publicKey
    }

    private fun getPrivateKey(): PrivateKey {
        return (entry as KeyStore.PrivateKeyEntry).privateKey
    }

    fun generateAndStoreKeys(context: Context): Boolean {
        try {
            val start = GregorianCalendar()
            val end = GregorianCalendar().apply { add(Calendar.YEAR, 10) }
            val kgen = KeyPairGenerator.getInstance(Config.KEY_ALGO, Config.ANDROID_KEYSTORE)
            val spec = KeyPairGeneratorSpec.Builder(context)
                .setKeySize(Config.KEY_SIZE)
                .setAlias(Config.keyname)
                .setSubject(X500Principal("CN=" + Config.keyname))
                .setSerialNumber(BigInteger.valueOf(Config.serialNr))
                .setStartDate(start.time)
                .setEndDate(end.time)
                .build()
            kgen.initialize(spec)
            kgen.generateKeyPair()
        }
        catch (ex: Exception) {
            return false
        }
        return true
    }

    fun signContent(content: ByteArray) : String? {
        if (content.isEmpty()) return null
        try {
            val entry = KeyStore.getInstance(Config.ANDROID_KEYSTORE).run {
                load(null)
                getEntry(Config.keyname, null)
            }
            val prKey = (entry as KeyStore.PrivateKeyEntry).privateKey
            val sg = Signature.getInstance(Config.SIGN_ALGO)
            sg.initSign(prKey)
            sg.update(content)
            return Base64.encodeToString(sg.sign(), Base64.DEFAULT)
        }
        catch  (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun decryptContent(content: String): String {
        if (content.isEmpty()) return ""
        val contentFromBase64 = Base64.decode(content, Base64.DEFAULT)
        val decryptedContent = Cipher.getInstance(Config.ENC_ALGO).run {
            init(Cipher.DECRYPT_MODE, getPrivateKey())
            doFinal(contentFromBase64)
        }

        return String(decryptedContent)
    }

    fun genRandomChallenge(): ByteArray {
        val content = ByteArray(20)
        SecureRandom().nextBytes(content)
        return content
    }
}