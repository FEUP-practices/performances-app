package pt.feup.performances.infrastructure.delivery

import com.google.gson.Gson
import io.swagger.models.Response
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.feup.performances.core.*
import pt.feup.performances.core.usecases.TicketsUseCase
import pt.feup.performances.core.usecases.UsersUseCase

/**
 * The specification of the controller.
 */
interface TicketsController {
    fun buyTickets(@RequestBody data: TicketIn): ResponseEntity<Ticket>
    fun validateTicket(@RequestBody data: TicketValidateIn): ResponseEntity<ValidateOut>
}

data class TicketValidateIn(val ticketId: String, val userId: String, val performanceId: String)

data class ValidateOut(val valid: Boolean)

/**
 * The implementation of the controller.
 *
 * **Note**: Spring Boot is able to discover this [RestController] without further configuration.
 */
@RestController
class TicketsControllerImpl(
    private val ticketsUseCase: TicketsUseCase,
    private val usersUseCase: UsersUseCase
) : TicketsController {
    @PostMapping("/tickets", consumes = [MediaType.APPLICATION_JSON_VALUE])
    override fun buyTickets(@RequestBody data: TicketIn): ResponseEntity<Ticket> {
        val signatureService = SignatureService()
        val user = usersUseCase.getUser(data.userId)
        val isValid = signatureService.verify(data.signature, user.pubKey)
        if (!isValid){
            throw InvalidSignatureException()
        }
        val ticket = ticketsUseCase.buyTicket(data)
        // **Important**: didn't find the way to encrypt the ticket with the public key of the user so
        // I will return the ticket as it is.
        return ResponseEntity.ok(ticket)
    }

    @PostMapping("/tickets/validate", consumes = [MediaType.APPLICATION_JSON_VALUE])
    override fun validateTicket(@RequestBody data: TicketValidateIn): ResponseEntity<ValidateOut> {
        val validated = ticketsUseCase.validateTicket(data.ticketId, data.userId, data.performanceId)
        return if (validated) {
            ResponseEntity.ok(ValidateOut(true))
        } else {
            ResponseEntity.notFound().build()
        }
    }

}
