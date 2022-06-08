package pt.feup.performances.core

import java.util.*

interface PerformancesRepositoryService {
    fun save(performance: PerformanceIn): Performance
    fun updateSeatsLeft(performanceId: String, seatsLeft: Int)
    fun findById(id: String): PerformanceDescription?
    fun findAll(numPage: Int): List<Performance>
}

interface TicketRepositoryService {
    fun save(ticket: TicketIn): Ticket
    fun findById(id: String): Ticket?
    fun findByUserId(userId: String): List<Ticket>
    fun findByPerformanceId(performanceId: String): List<Ticket>
    fun findByIdAndUpdateUsed(id: String): Ticket?
    fun countTicketsByUserAndPerformance(userId: String, performanceId: String): Long
}

interface UserRepositoryService {
    fun save(user: User): String
    fun findById(id: String): User?
}