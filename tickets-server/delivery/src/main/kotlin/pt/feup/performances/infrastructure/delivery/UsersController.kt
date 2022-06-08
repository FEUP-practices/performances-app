package pt.feup.performances.infrastructure.delivery

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.feup.performances.core.InvalidSignatureException
import pt.feup.performances.core.Signature
import pt.feup.performances.core.User
import pt.feup.performances.core.usecases.UsersUseCase
import pt.feup.performances.infrastructure.delivery.middleware.SecureStore
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.Base64


/**
 * The specification of the controller.
 */
interface UsersController {
    fun registerUser(registerIn: RegisterIn): ResponseEntity<RegisterOut>
}

data class RegisterOut(val userId: String)

data class RegisterIn(val user: User, val signature: Signature)

/**
 * The implementation of the controller.
 *
 * **Note**: Spring Boot is able to discover this [RestController] without further configuration.
 */
@RestController
class UsersControllerImpl(
    private val usersUseCase: UsersUseCase
) : UsersController {
    @PostMapping("/users", consumes = [MediaType.APPLICATION_JSON_VALUE])
    override fun registerUser(@RequestBody registerIn: RegisterIn): ResponseEntity<RegisterOut> {
        val isValid = SignatureService().verify(registerIn.signature, registerIn.user.pubKey)
        if (!isValid) {
            throw InvalidSignatureException()
        }
        val result = usersUseCase.registerUser(registerIn.user)
        return ResponseEntity.ok(RegisterOut(result))
    }

}