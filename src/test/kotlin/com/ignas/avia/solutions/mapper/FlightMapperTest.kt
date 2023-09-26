package com.ignas.avia.solutions.mapper

import com.ignas.avia.solutions.enums.FlightStatus
import com.ignas.avia.solutions.stub.FlightStub
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FlightMapperTest {

    @Test
    fun `map scheduled flight to entity`() {
        val scheduledFlight = FlightStub.scheduledFlightStub()
        val flightEntity = FlightMapper.mapScheduledFlightToEntity(scheduledFlight)

        assertEquals(scheduledFlight.flightNumber, flightEntity.flightNumber)
        assertEquals(scheduledFlight.aircraftType, flightEntity.aircraftType)
        assertEquals(scheduledFlight.origin, flightEntity.origin)
        assertEquals(scheduledFlight.destination, flightEntity.destination)
        assertNotNull(flightEntity.departureTime)
        assertNotNull(flightEntity.arrivalTime)
        assertEquals(scheduledFlight.status, flightEntity.status)
    }

    @Test
    fun `map scheduled flight to FlightDto`() {
        val scheduledFlight = FlightStub.flightEntityStub(FlightStatus.SCHEDULED)
        val flightDto = FlightMapper.mapEntityToFlightDto(scheduledFlight)

        assertEquals(scheduledFlight.flightNumber, flightDto.flightNumber)
        assertEquals(scheduledFlight.aircraftType, flightDto.aircraftType)
        assertEquals(scheduledFlight.origin, flightDto.origin)
        assertEquals(scheduledFlight.destination, flightDto.destination)
        assertNotNull(flightDto.estimatedDepartureTime)
        assertNotNull(flightDto.estimatedArrivalTime)
        assertNull(flightDto.departureTime)
        assertNull(flightDto.arrivalTime)
        assertEquals(scheduledFlight.status, flightDto.status)
    }

    @Test
    fun `map departed flight to FlightDto`() {
        val scheduledFlight = FlightStub.flightEntityStub(FlightStatus.DEPARTED)
        val flightDto = FlightMapper.mapEntityToFlightDto(scheduledFlight)

        assertEquals(scheduledFlight.flightNumber, flightDto.flightNumber)
        assertEquals(scheduledFlight.aircraftType, flightDto.aircraftType)
        assertEquals(scheduledFlight.origin, flightDto.origin)
        assertEquals(scheduledFlight.destination, flightDto.destination)
        assertNull(flightDto.estimatedDepartureTime)
        assertNotNull(flightDto.estimatedArrivalTime)
        assertNotNull(flightDto.departureTime)
        assertNull(flightDto.arrivalTime)
        assertEquals(scheduledFlight.status, flightDto.status)
    }

    @Test
    fun `map arrived flight to FlightDto`() {
        val scheduledFlight = FlightStub.flightEntityStub(FlightStatus.ARRIVED)
        val flightDto = FlightMapper.mapEntityToFlightDto(scheduledFlight)

        assertEquals(scheduledFlight.flightNumber, flightDto.flightNumber)
        assertEquals(scheduledFlight.aircraftType, flightDto.aircraftType)
        assertEquals(scheduledFlight.origin, flightDto.origin)
        assertEquals(scheduledFlight.destination, flightDto.destination)
        assertNull(flightDto.estimatedDepartureTime)
        assertNull(flightDto.estimatedArrivalTime)
        assertNotNull(flightDto.departureTime)
        assertNotNull(flightDto.arrivalTime)
        assertEquals(scheduledFlight.status, flightDto.status)
    }
}