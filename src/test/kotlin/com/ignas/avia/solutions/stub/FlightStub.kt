package com.ignas.avia.solutions.stub

import com.ignas.avia.solutions.entity.Flight
import com.ignas.avia.solutions.enums.FlightStatus
import com.ignas.avia.solutions.model.ScheduledFlight
import java.time.OffsetDateTime

object FlightStub {

    fun scheduledFlightStub(
        flightNumber: String = "LH888",
        aircraftType: String = "Boeing 737",
        origin: String = "Frankfurt FRA",
        destination: String = "Vilnius VNO",
        estimatedDepartureTime: OffsetDateTime = OffsetDateTime.now(),
        estimatedArrivalTime: OffsetDateTime = OffsetDateTime.now().plusHours(3),
        status: FlightStatus = FlightStatus.SCHEDULED
    ): ScheduledFlight {
        return ScheduledFlight(
            flightNumber = flightNumber,
            aircraftType = aircraftType,
            origin = origin,
            destination = destination,
            estimatedDepartureTime = estimatedDepartureTime,
            estimatedArrivalTime = estimatedArrivalTime,
            status = status
        )
    }

    fun flightEntityStub(status: FlightStatus): Flight {
        return Flight(
            flightNumber = "FF123",
            aircraftType = "Cessna C172",
            origin = "Paluknys",
            destination = "Vilnius",
            departureTime = OffsetDateTime.now(),
            arrivalTime = OffsetDateTime.now().plusMinutes(30),
            status = status
        )
    }
}