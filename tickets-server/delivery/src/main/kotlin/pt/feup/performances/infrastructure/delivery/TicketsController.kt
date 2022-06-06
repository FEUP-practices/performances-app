package pt.feup.performances.infrastructure.delivery

import io.swagger.models.Response
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.feup.performances.core.Ticket
import pt.feup.performances.core.TicketIn
import pt.feup.performances.core.usecases.TicketsUseCase

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
    private val ticketsUseCase: TicketsUseCase
) : TicketsController {
    @PostMapping("/tickets", consumes = [MediaType.APPLICATION_JSON_VALUE])
    override fun buyTickets(@RequestBody data: TicketIn): ResponseEntity<Ticket> {
        println(data)
        val ticket = ticketsUseCase.buyTicket(data)
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
