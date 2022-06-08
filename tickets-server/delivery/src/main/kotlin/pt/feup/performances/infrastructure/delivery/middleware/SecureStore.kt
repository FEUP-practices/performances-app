package pt.feup.performances.infrastructure.delivery.middleware

import org.springframework.stereotype.Component
import pt.feup.performances.core.InvalidSignatureException
import java.security.PublicKey
import java.security.Signature
import javax.crypto.Cipher

@Component
class SecureStore {
    fun verifySignature(content: ByteArray, result: ByteArray, pubKey: PublicKey): Boolean {
        if (content.isEmpty()) return false
        try {
            val sg = Signature.getInstance(Config.SIGN_ALGO)
            sg.initVerify(pubKey)
            sg.update(content)
            return sg.verify(result)
        } catch (e: Exception) {
            e.printStackTrace()
            throw InvalidSignatureException()
        }
    }

    fun encryptContent(content: ByteArray, pubKey: PublicKey): String {
        if (content.isEmpty()) throw InvalidSignatureException()
        try {
            val cipher = Cipher.getInstance(Config.ENC_ALGO)
            cipher.init(Cipher.ENCRYPT_MODE, pubKey)
            return String(cipher.doFinal(content))
        } catch (e: Exception) {
            e.printStackTrace()
            throw InvalidSignatureException()
        }
    }
}