package pt.feup.performances.infrastructure.repositories

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user")
class UserEntity(
        @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid", strategy = "uuid")
        @Column(columnDefinition = "CHAR(32)")
        @Id
        val id: String?,
        val nif: String,
        val name: String,
        val cardType: String,
        val cardNumber: String,
        val cardValidity: String,
        val cardCVV: String,
        val pubKey: ByteArray,
)

@Entity
@Table(name = "ticket")
class TicketEntity(
    @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid", strategy = "uuid")
        @Column(columnDefinition = "CHAR(32)")
        @Id
        val id: String?,
    val seatNumber: Int,
    var used: Boolean,
    val numberBought: Int,
    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "id")
    val user: UserEntity,
    @ManyToOne
    @JoinColumn(name = "performance", referencedColumnName = "id")
    val performance: PerformanceEntity,
)

@Entity
@Table(name = "performance")
class PerformanceEntity(
    @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid", strategy = "uuid")
        @Column(columnDefinition = "CHAR(32)")
        @Id
        val id: String?,
    val endDate: Date,
    val description: String,
    val price: Double,
    var seatsLeft: Int,
    val imageURI: String,
    val name: String,
    val startDate: Date,
    val address: String,
)
