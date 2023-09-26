package com.ignas.avia.solutions.model

import com.ignas.avia.solutions.enums.FlightStatus
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime

data class ScheduledFlight(
    val flightNumber: String,
    val aircraftType: String,
    val origin: String,
    val destination: String,
    val estimatedDepartureTime: OffsetDateTime,
    val estimatedArrivalTime: OffsetDateTime,
    val status: FlightStatus = FlightStatus.SCHEDULED
)