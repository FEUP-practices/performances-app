package pt.feup.performances

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pt.feup.performances.core.usecases.*
import pt.feup.performances.infrastructure.repositories.*


/**
 * Wires use cases with service implementations, and services implementations with repositories.
 *
 * **Note**: Spring Boot is able to discover this [Configuration] without further configuration.
 */
@Configuration
class ApplicationConfiguration(
    @Autowired val performanceRepository: PerformanceEntityRepository,
    @Autowired val ticketRepository: TicketEntityRepository,
    @Autowired val userRepository: UserEntityRepository
    ){

    // Services
    @Bean
    fun performanceRepositoryService() = PerformancesRepositoryServiceImpl(performanceRepository)
    @Bean
    fun ticketRepositoryService() = TicketRepositoryServiceImpl(ticketRepository, userRepository, performanceRepository)
    @Bean
    fun userRepositoryService() = UserRepositoryServiceImpl(userRepository)

    // Use cases
    @Bean
    fun performanceUseCase() = PerformancesUseCaseImpl(performanceRepositoryService())
    @Bean
    fun ticketUseCase() = TicketsUseCaseImpl(ticketRepositoryService(), performanceRepositoryService())
    @Bean
    fun userUseCase() = UsersUseCaseImpl(userRepositoryService())
}