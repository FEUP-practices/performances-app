package pt.feup.performances.core.usecases

import pt.feup.performances.core.*

interface PerformancesUseCase {
    fun fetchPerformances(numPage: Int) : List<Performance>
    fun fetchPerformance(id: String) : PerformanceDescription
    fun postPerformance(performance: PerformanceIn): Boolean
}

class PerformancesUseCaseImpl(
    private val performaceRepository: PerformancesRepositoryService
) : PerformancesUseCase {
    override fun fetchPerformances(numPage: Int): List<Performance> = performaceRepository.findAll(numPage)

    override fun fetchPerformance(id: String): PerformanceDescription = performaceRepository.findById(id) ?: throw PerformanceNotFoundException()

    override fun postPerformance(performance: PerformanceIn): Boolean {
        performaceRepository.save(performance)
        return true
    }
}