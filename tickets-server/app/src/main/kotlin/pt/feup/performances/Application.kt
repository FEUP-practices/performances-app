package pt.feup.performances

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * The marker that makes this project a Spring Boot application.
 */
@SpringBootApplication
class PerformancesApplication

/**
 * The main entry point.
 */
fun main(args: Array<String>) {
    runApplication<PerformancesApplication>(*args)
}
