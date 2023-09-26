package com.ignas.avia.solutions.entity

import com.ignas.avia.solutions.enums.FlightStatus
import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
class Flight (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    val flightNumber: String,
    val aircraftType: String,
    val origin: String,
    val destination: String,
    var departureTime: OffsetDateTime,
    var arrivalTime: OffsetDateTime,

    @Enumerated(EnumType.STRING)
    var status: FlightStatus
)