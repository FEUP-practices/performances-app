package pt.feup.performances.infrastructure.delivery

import pt.feup.performances.core.InvalidSignatureException
import pt.feup.performances.core.Signature
import pt.feup.performances.infrastructure.delivery.middleware.Config
import pt.feup.performances.infrastructure.delivery.middleware.SecureStore
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class SignatureService {
    fun encrypt(contentStr: String, pubKeyStr: String): String {
        try {
            val publicKeyBA = Base64.getMimeDecoder().decode(pubKeyStr)
            val pubKey = KeyFactory.getInstance(Config.KEY_ALGO).generatePublic(X509EncodedKeySpec(publicKeyBA));
            return SecureStore().encryptContent(gzip(contentStr), pubKey)
        } catch (e: Exception) {
            throw InvalidSignatureException()
        }

    }

    fun verify(signatureBA: Signature, pubKeyStr: String): Boolean {
        try {
            val publicKeyBA = Base64.getMimeDecoder().decode(pubKeyStr)
            val pubKey = KeyFactory.getInstance(Config.KEY_ALGO).generatePublic(X509EncodedKeySpec(publicKeyBA));
            val challenge = Base64.getMimeDecoder().decode(signatureBA.challenge)
            val signature = Base64.getMimeDecoder().decode(signatureBA.signature)
            return SecureStore().verifySignature(challenge, signature, pubKey)
        } catch (e: Exception) {
            throw InvalidSignatureException()
        }
    }

    private fun gzip(content: String): ByteArray {
        val bos = ByteArrayOutputStream()
        GZIPOutputStream(bos).bufferedWriter(StandardCharsets.UTF_8).use { it.write(content) }
        return bos.toByteArray()
    }

    fun ungzip(content: ByteArray): String =
        GZIPInputStream(content.inputStream()).bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
}