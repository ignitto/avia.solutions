package com.ignas.avia.solutions.mapper

import com.ignas.avia.solutions.entity.Flight
import com.ignas.avia.solutions.enums.FlightStatus
import com.ignas.avia.solutions.model.FlightDto
import com.ignas.avia.solutions.model.ScheduledFlight

object FlightMapper {

    fun mapScheduledFlightToEntity(scheduledFlight: ScheduledFlight): Flight {
        return Flight(
            flightNumber = scheduledFlight.flightNumber,
            aircraftType = scheduledFlight.aircraftType,
            origin = scheduledFlight.origin,
            destination = scheduledFlight.destination,
            departureTime = scheduledFlight.estimatedDepartureTime,
            arrivalTime = scheduledFlight.estimatedArrivalTime,
            status = scheduledFlight.status
        )
    }

    fun mapEntityToFlightDto(flight: Flight): FlightDto {
        return FlightDto(
            flightNumber = flight.flightNumber,
            aircraftType = flight.aircraftType,
            origin = flight.origin,
            destination = flight.destination,
            estimatedArrivalTime = if (flight.status == FlightStatus.SCHEDULED || flight.status == FlightStatus.DEPARTED) flight.arrivalTime else null,
            estimatedDepartureTime = if (flight.status == FlightStatus.SCHEDULED) flight.departureTime else null,
            arrivalTime = if (flight.status == FlightStatus.ARRIVED) flight.arrivalTime else null,
            departureTime = if (flight.status == FlightStatus.DEPARTED || flight.status == FlightStatus.ARRIVED) flight.arrivalTime else null,
            status = flight.status
        )
    }
}