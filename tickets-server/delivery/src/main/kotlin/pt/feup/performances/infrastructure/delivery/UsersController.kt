package pt.feup.performances.infrastructure.delivery

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.feup.performances.core.User
import pt.feup.performances.core.usecases.UsersUseCase


/**
 * The specification of the controller.
 */
interface UsersController {
    fun registerUser(user: User): ResponseEntity<RegisterOut>
}

data class RegisterOut(val userId: String)

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
    override fun registerUser(@RequestBody user: User): ResponseEntity<RegisterOut> {
        val result = usersUseCase.registerUser(user)
        return ResponseEntity.ok(RegisterOut(result))
    }

}