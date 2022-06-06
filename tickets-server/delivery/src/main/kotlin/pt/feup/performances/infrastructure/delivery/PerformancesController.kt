package pt.feup.performances.infrastructure.delivery

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.feup.performances.core.Performance
import pt.feup.performances.core.PerformanceDescription
import pt.feup.performances.core.PerformanceIn
import pt.feup.performances.core.usecases.PerformancesUseCase

/**
 * The specification of the controller.
 */
interface PerformancesController {
    fun fetchAllPerformances(data: AllPerformancesIn): ResponseEntity<List<Performance>>
    fun postPerformance(data: PerformanceIn): ResponseEntity<Boolean>
    fun fetchPerformance(id: String): ResponseEntity<PerformanceDescription>
}

data class AllPerformancesIn (val numPage: Int)

/**
 * The implementation of the controller.
 *
 * **Note**: Spring Boot is able to discover this [RestController] without further configuration.
 */
@RestController
class PerformancesControllerImpl(
    private val performancesUseCase: PerformancesUseCase
) : PerformancesController {
    @GetMapping("/performances", produces = [MediaType.APPLICATION_JSON_VALUE])
    override fun fetchAllPerformances(data: AllPerformancesIn): ResponseEntity<List<Performance>> {
        val listPerformances = performancesUseCase.fetchPerformances(data.numPage)
        return ResponseEntity.ok(listPerformances)
    }

    @PostMapping("/performances")
    override fun postPerformance(@RequestBody data: PerformanceIn): ResponseEntity<Boolean> {
        println(data)
        return if (performancesUseCase.postPerformance(data))
            ResponseEntity.ok(true)
        else
            ResponseEntity.badRequest().build()
    }

    @GetMapping("/performances/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    override fun fetchPerformance(@PathVariable id: String): ResponseEntity<PerformanceDescription> {
        val performance = performancesUseCase.fetchPerformance(id)
        return ResponseEntity.ok(performance)
    }

}