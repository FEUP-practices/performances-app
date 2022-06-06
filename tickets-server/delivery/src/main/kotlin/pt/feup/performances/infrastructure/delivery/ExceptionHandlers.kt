package pt.feup.performances.infrastructure.delivery

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import pt.feup.performances.core.*
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ResponseBody
    @ExceptionHandler(value = [UserNotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected fun userNotFound(ex: UserNotFoundException) = ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.message)

    @ResponseBody
    @ExceptionHandler(value = [PerformanceNotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected fun performanceNotFound(ex: PerformanceNotFoundException) = ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.message)

    @ResponseBody
    @ExceptionHandler(value = [TicketNotFoundException::class])
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected fun ticketNotVerified(ex: TicketNotFoundException) = ErrorMessage(HttpStatus.UNAUTHORIZED.value(), ex.message)

    @ResponseBody
    @ExceptionHandler(value = [UnauthorizedException::class])
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected fun unAuthorized(ex: UnauthorizedException) = ErrorMessage(HttpStatus.UNAUTHORIZED.value(), ex.message)

    @ResponseBody
    @ExceptionHandler(value = [NotEnoughSeatsException::class])
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected fun notEnoughSeats(ex: NotEnoughSeatsException) = ErrorMessage(HttpStatus.FORBIDDEN.value(), ex.message)

    @ResponseBody
    @ExceptionHandler(value = [TicketAlreadyUsedException::class])
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected fun ticketAlreadyUsed(ex: TicketAlreadyUsedException) = ErrorMessage(HttpStatus.FORBIDDEN.value(), ex.message)

    @ResponseBody
    @ExceptionHandler(value = [TicketAlreadyExpiredException::class])
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected fun ticketAlreadyExpired(ex: TicketAlreadyExpiredException) = ErrorMessage(HttpStatus.FORBIDDEN.value(), ex.message)
}

data class ErrorMessage(
    val statusCode: Int,
    val message: String?,
    val timestamp: String = DateTimeFormatter.ISO_DATE_TIME.format(OffsetDateTime.now())
)