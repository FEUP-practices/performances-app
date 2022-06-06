package pt.feup.performances.infrastructure.repositories

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import pt.feup.performances.core.*
import java.util.*

const val DEFAULT_PAGE_SIZE = 10

class PerformancesRepositoryServiceImpl (
    private val performancesRepository: PerformanceEntityRepository
        ) : PerformancesRepositoryService {
    override fun save(performance: PerformanceIn): Performance = performancesRepository.save(performance.toEntity()).toDomain()
    override fun updateSeatsLeft(performanceId: String, seatsLeft: Int) {
        val performance = performancesRepository.findById(performanceId).orElse(null) ?: throw PerformanceNotFoundException()
        performance.seatsLeft -= seatsLeft
        performancesRepository.save(performance)
    }
    override fun findById(id: String): PerformanceDescription = (performancesRepository.findById(id).orElse(null) ?: throw PerformanceNotFoundException()).toDomainWithDescription()
    override fun findAll(numPage: Int): List<Performance> {
    val nextTenElements: PageRequest = PageRequest.of(numPage, DEFAULT_PAGE_SIZE, Sort.Direction.ASC, "startDate")
       return performancesRepository.findAll(nextTenElements).map { it.toDomain() }.toList()
    }
        }

class TicketRepositoryServiceImpl (
    private val ticketsRepository: TicketEntityRepository,
    private val userRepository: UserEntityRepository,
    private val performanceRepository: PerformanceEntityRepository
        ) : TicketRepositoryService {
    override fun save(ticket: TicketIn): Ticket {
        val user = userRepository.findById(ticket.userId).orElse(null) ?: throw UserNotFoundException()
        val performance = performanceRepository.findById(ticket.performanceId).orElse(null) ?: throw PerformanceNotFoundException()
        return ticketsRepository.save(ticket.toEntity(user, performance)).toDomain()
    }
    override fun countTicketsByUserAndPerformance(userId: String, performanceId: String): Long {
        return ticketsRepository.findAllByPerformanceIdAndUserId(performanceId, userId).fold(0) { acc, ticket -> acc + ticket.numberBought }
    }
    override fun findById(id: String): Ticket? = ticketsRepository.findById(id).orElse(null)?.toDomain()
    override fun findByUserId(userId: String): List<Ticket> = ticketsRepository.findByUserNif(userId).map { it.toDomain() }.toList()
    override fun findByPerformanceId(performanceId: String): List<Ticket> = ticketsRepository.findByPerformanceId(performanceId).map { it.toDomain() }.toList()
    override fun findByIdAndUpdateUsed(id: String): Ticket? = ticketsRepository.findById(id).orElse(null)?.let {
        if (it.used) {
            throw TicketAlreadyUsedException(id)
        }
        it.used = true
        if (it.performance.endDate.before(Date())) {
            throw TicketAlreadyExpiredException(id)
        }
        ticketsRepository.save(it)
    }?.toDomain()
}

class UserRepositoryServiceImpl (
    private val userRepository: UserEntityRepository
        ) : UserRepositoryService {
    override fun save(user: User): String = userRepository.save(user.toEntity()).toDomain().id!!
}
