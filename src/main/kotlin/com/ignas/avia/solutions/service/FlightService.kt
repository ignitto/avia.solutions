package com.ignas.avia.solutions.service

import com.ignas.avia.solutions.enums.FlightStatus
import com.ignas.avia.solutions.exception.FlightNotFoundException
import com.ignas.avia.solutions.exception.IllegalDateTimeException
import com.ignas.avia.solutions.mapper.FlightMapper
import com.ignas.avia.solutions.model.FlightDto
import com.ignas.avia.solutions.model.ScheduledFlight
import com.ignas.avia.solutions.repository.FlightRepository
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.logging.Logger

@Service
class FlightService(
    val flightRepository: FlightRepository,
    val terminalService: TerminalService
) {

    companion object {
        val logger = Logger.getLogger(FlightService::class.java.name)
    }

    fun registerScheduledFlight(scheduledFlight: ScheduledFlight): ScheduledFlight {
        if (scheduledFlight.estimatedDepartureTime.isAfter(scheduledFlight.estimatedArrivalTime)) {
            throw IllegalDateTimeException("Estimated Departure Time cannot be after Estimated Arrival Time")
        }

        val flightEntity = FlightMapper.mapScheduledFlightToEntity(scheduledFlight)
        flightRepository.save(flightEntity)
        logger.info("Scheduled flight ${scheduledFlight.flightNumber} has been registered")
        return scheduledFlight
    }

    fun updateDepartureTime(flightNumber: String): FlightDto {
        val flight = flightRepository.findFlightsByFlightNumberAndStatus(flightNumber, FlightStatus.SCHEDULED)
            ?: throw FlightNotFoundException("Scheduled flight number=[$flightNumber] was not found")

        flight.departureTime = OffsetDateTime.now()
        flight.status = FlightStatus.DEPARTED
        val updatedFlight = flightRepository.save(flight)
        logger.info("Scheduled flight number=[$flightNumber] has been departed")

        return FlightMapper.mapEntityToFlightDto(updatedFlight)
    }

    fun updateArrivalTime(flightNumber: String): FlightDto {
        val flight = flightRepository.findFlightsByFlightNumberAndStatus(flightNumber, FlightStatus.DEPARTED)
            ?: throw FlightNotFoundException("Departed flight number=[$flightNumber] was not found")

        flight.arrivalTime = OffsetDateTime.now()
        flight.status = FlightStatus.ARRIVED
        val updatedFlight = flightRepository.save(flight)
        logger.info("Scheduled flight number=[$flightNumber] has arrived")

        terminalService.addToArrivalList(flightNumber)

        return FlightMapper.mapEntityToFlightDto(updatedFlight)
    }

    fun getFlightStatus(flightNumber: String): FlightStatus {
        val flight = flightRepository.findFlightsByFlightNumberOrderByIdDesc(flightNumber)
            ?: throw FlightNotFoundException("Flight number=[$flightNumber] was not found")
        return flight.status
    }

    //TODO: Pagination response
    fun getAllRegisteredFlights(): List<FlightDto> {
        val flights = flightRepository.findAll()
        return flights.map { FlightMapper.mapEntityToFlightDto(it) }
    }

    //TODO: implement search criteria
    fun searchFlights(origin: String?, destination: String?): List<FlightDto> {
        if (origin != null && destination != null) {
            val flights = flightRepository.findFlightsByOriginAndDestination(origin, destination)
            return flights.map { FlightMapper.mapEntityToFlightDto(it) }
        }
        if (origin != null) {
            val flights = flightRepository.findFlightsByOrigin(origin)
            return flights.map { FlightMapper.mapEntityToFlightDto(it) }
        }
        if (destination != null) {
            val flights = flightRepository.findFlightsByDestination(destination)
            return flights.map { FlightMapper.mapEntityToFlightDto(it) }
        }

        return listOf();
    }
}