package pt.feup.performances.core

class NotEnoughSeatsException() : Exception("There are not enough seats")

class TooManySeatsException() : Exception("Bought too many seats")

class UserNotFoundException() : Exception("User not registered or recognized")

class TicketNotFoundException(private val id: String) : Exception("Ticket with [$id] not found")

class PerformanceNotFoundException() : Exception("Performance not found")

class TicketAlreadyUsedException(private val id: String) : Exception("Ticket with [$id] already used")

class TicketAlreadyExpiredException(private val id: String) : Exception("Ticket with [$id] already expired")

class TicketNotVerifiedException() : Exception("Ticket not verified")

class UnauthorizedException() : Exception("Unauthorized")
