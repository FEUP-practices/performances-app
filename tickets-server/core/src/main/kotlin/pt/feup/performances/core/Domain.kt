package pt.feup.performances.core

import java.time.OffsetDateTime
import java.util.*

data class Performance(
        val id: String?,
        val name: String,
        val startDate: String,
        val endDate: String,
        val address: String,
        val price: Double,
        val seatsLeft: Int,
        var imageURI: String = "https://bocadolobo.com/blog/wp-content/uploads/2020/10/Some-of-The-Most-Famous-Artists-Of-All-Time-3.jpg"
)

data class PerformanceIn(
        val name: String,
        val startDate: String,
        val endDate: String,
        val address: String,
        val price: Double,
        val seatsLeft: Int,
        var imageURI: String = "https://bocadolobo.com/blog/wp-content/uploads/2020/10/Some-of-The-Most-Famous-Artists-Of-All-Time-3.jpg",
        val description: String,
)

data class PerformanceDescription(
        val id: String,
        val description: String,
        val seatsLeft: Int
)

data class TicketIn(
        val userId: String,
        val performanceId: String,
        val seatNumber: Int,
        val numberBought: Int
)

data class Ticket(
        val id: String,
        val userId: String,
        val seatNumber: Int,
        val performance: Performance,
        val used: Boolean,
        val numberBought: Int
)

data class User(
        val id: String?,
        val nif: String,
        val name: String,
        val cardType: String,
        val cardNumber: String,
        val cardValidity: String,
        val cardCVV: String
)

data class UserIn(
        val nif: String,
        val name: String,
        val cardType: String,
        val cardNumber: String,
        val cardValidity: String,
        val cardCVV: String,
        val signature: String,
        val challenge: String,
        val pubKey: String
)

