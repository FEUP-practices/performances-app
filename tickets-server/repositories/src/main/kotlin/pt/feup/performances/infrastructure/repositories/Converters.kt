package pt.feup.performances.infrastructure.repositories

import pt.feup.performances.core.*
import java.text.SimpleDateFormat

val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

fun PerformanceIn.toEntity() = PerformanceEntity(
    id = null,
    name = name,
    startDate = dateFormat.parse(startDate),
    address = address,
    description = description,
    endDate = dateFormat.parse(endDate),
    price = price,
    seatsLeft = seatsLeft,
    imageURI = imageURI,
)

fun PerformanceEntity.toDomain() = Performance(
    id = id,
    name = name,
    startDate = startDate.toString(),
    endDate = endDate.toString(),
    address = address,
    price = price,
    seatsLeft = seatsLeft,
    imageURI = imageURI,
)

fun PerformanceEntity.toDomainWithDescription() = PerformanceDescription (
    id = id!!,
    description = description,
        seatsLeft = seatsLeft,)

fun TicketIn.toEntity(userEntity: UserEntity, performanceEntity: PerformanceEntity) = TicketEntity(
    id = null,
    seatNumber = seatNumber,
    used = false,
    user = userEntity,
    performance = performanceEntity,
    numberBought = numberBought
)

fun TicketEntity.toDomain() = Ticket (
    id = id!!,
    used = used,
    seatNumber = seatNumber,
    performance = performance.toDomain(),
    numberBought = numberBought,
    userId = user.id!!
)

fun User.toEntity() = UserEntity(
    nif = nif,
    name = name,
    cardType = cardType,
    cardNumber = cardNumber,
    cardValidity = cardValidity,
    cardCVV = cardCVV,
    id = null
)

fun UserEntity.toDomain() = User(
    nif = nif,
    name = name,
    cardType = cardType,
    cardNumber = cardNumber,
    cardValidity = cardValidity,
    cardCVV = cardCVV,
    id = id
)
