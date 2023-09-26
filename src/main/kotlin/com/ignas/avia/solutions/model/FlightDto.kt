package com.ignas.avia.solutions.model

import com.ignas.avia.solutions.enums.FlightStatus
import java.time.OffsetDateTime

class FlightDto(
    val flightNumber: String,
    val aircraftType: String,
    val origin: String,
    val destination: String,
    val estimatedDepartureTime: OffsetDateTime? = null,
    val estimatedArrivalTime: OffsetDateTime? = null,
    val departureTime: OffsetDateTime? = null,
    val arrivalTime: OffsetDateTime? = null,
    val status: FlightStatus
)