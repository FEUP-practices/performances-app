package pt.feup.performances.core.usecases

import pt.feup.performances.core.*

const val MAX_NUMBER_OF_PERFORMANCES = 5

interface TicketsUseCase {
    fun buyTicket(ticket: TicketIn): Ticket
    fun validateTicket(ticketId: String, userId: String, performanceId: String): Boolean
}

class TicketsUseCaseImpl(
    private val ticketsRepository: TicketRepositoryService,
    private val performancesRepository: PerformancesRepositoryService
) : TicketsUseCase {
    override fun buyTicket(ticket: TicketIn): Ticket {
        val performance = performancesRepository.findById(ticket.performanceId)
        val numberOfPerformances = ticketsRepository.countTicketsByUserAndPerformance(ticket.userId, ticket.performanceId)
        if (numberOfPerformances >= MAX_NUMBER_OF_PERFORMANCES) {
            throw TooManySeatsException()
        }
        if (performance!!.seatsLeft < ticket.numberBought) {
            throw NotEnoughSeatsException()
        }
        val ticketOut = ticketsRepository.save(ticket)
        performancesRepository.updateSeatsLeft(ticket.performanceId, ticket.numberBought)
        return ticketOut
    }
    override fun validateTicket(ticketId: String, userId: String, performanceId: String): Boolean {
        val ticket = ticketsRepository.findByIdAndUpdateUsed(ticketId) ?: throw TicketNotFoundException(ticketId)
        if (ticket.userId != userId || ticket.performance.id != performanceId) {
            throw TicketNotVerifiedException()
        }
        return true
    }
}
