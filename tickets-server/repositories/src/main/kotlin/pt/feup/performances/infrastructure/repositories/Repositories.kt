package pt.feup.performances.infrastructure.repositories

import org.springframework.data.jpa.repository.JpaRepository

interface UserEntityRepository : JpaRepository<UserEntity, String>

interface TicketEntityRepository : JpaRepository<TicketEntity, String> {
    fun findByUserNif(userId: String): List<TicketEntity>
    fun findByPerformanceId(performanceId: String): List<TicketEntity>
    fun findAllByPerformanceIdAndUserId(performanceId: String, userId: String): List<TicketEntity>
}

interface PerformanceEntityRepository : JpaRepository<PerformanceEntity, String>