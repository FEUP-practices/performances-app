package pt.feup.performances.infrastructure.delivery.middleware

import org.springframework.stereotype.Component
import pt.feup.performances.core.TicketNotVerifiedException
import java.security.KeyStore
import java.security.PublicKey
import java.security.Signature

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
            throw TicketNotVerifiedException()
        }
    }

    fun signContent(content: ByteArray, pubKey: PublicKey) : ByteArray {
        if (content.isEmpty()) return ByteArray(0)
        try {
            val entry = KeyStore.getInstance(Config.ANDROID_KEYSTORE).run {
                load(null)
                getEntry(Config.keyname, null)
            }
            val sg = Signature.getInstance(Config.SIGN_ALGO)
            //sg.initSign(pubKey)
            sg.update(content)
            return sg.sign()
        }
        catch  (e: Exception) {
            e.printStackTrace()
            throw TicketNotVerifiedException()
        }
    }
}