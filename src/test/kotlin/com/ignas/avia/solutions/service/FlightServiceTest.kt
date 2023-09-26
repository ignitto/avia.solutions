package com.ignas.avia.solutions.service

import com.ignas.avia.solutions.enums.FlightStatus
import com.ignas.avia.solutions.exception.FlightNotFoundException
import com.ignas.avia.solutions.exception.IllegalDateTimeException
import com.ignas.avia.solutions.mapper.FlightMapper
import com.ignas.avia.solutions.repository.FlightRepository
import com.ignas.avia.solutions.stub.FlightStub
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.time.OffsetDateTime

class FlightServiceTest {

    private lateinit var flightService: FlightService
    private lateinit var flightRepository: FlightRepository
    private lateinit var terminalService: TerminalService

    @BeforeEach
    fun init() {
        flightRepository = spy(FlightRepository::class.java)
        terminalService = mock(TerminalService::class.java)
        flightService = FlightService(flightRepository, terminalService)
    }

    @Test
    fun `register scheduled flight SUCCESS`() {
        val scheduledFlight = FlightStub.scheduledFlightStub()
        flightService.registerScheduledFlight(scheduledFlight)

        verify(flightRepository, times(1)).save(any())
    }

    @Test
    fun `register scheduled flight FAIL IllegalDateTimeException`() {
        val scheduledFlight = FlightStub.scheduledFlightStub(estimatedDepartureTime = OffsetDateTime.now().plusDays(1))
        assertThrows<IllegalDateTimeException> {
            flightService.registerScheduledFlight(scheduledFlight)
        }
        verify(flightRepository, never()).save(any())
    }

    @Test
    fun `update departure time SUCCESS`() {
        val scheduledFlight = FlightStub.scheduledFlightStub()
        val flight = FlightMapper.mapScheduledFlightToEntity(scheduledFlight)

        `when`(
            flightRepository.findFlightsByFlightNumberAndStatus(
                scheduledFlight.flightNumber,
                FlightStatus.SCHEDULED
            )
        ).thenReturn(flight)
        `when`(flightRepository.save(any())).thenReturn(flight)

        val updatedFlight = flightService.updateDepartureTime(scheduledFlight.flightNumber)

        assertEquals(updatedFlight.status, FlightStatus.DEPARTED)
        assertTrue(updatedFlight.departureTime!!.isAfter(scheduledFlight.estimatedDepartureTime))
    }

    @Test
    fun `update departure time FAIL FlightNotFoundException`() {
        assertThrows<FlightNotFoundException> {
            flightService.updateDepartureTime("FR123")
        }
        verify(flightRepository, never()).save(any())
    }

    @Test
    fun `update arrival time SUCCESS`() {
        val scheduledFlight = FlightStub.scheduledFlightStub(status = FlightStatus.DEPARTED)
        val flight = FlightMapper.mapScheduledFlightToEntity(scheduledFlight)

        `when`(
            flightRepository.findFlightsByFlightNumberAndStatus(
                scheduledFlight.flightNumber,
                FlightStatus.DEPARTED
            )
        ).thenReturn(flight)
        `when`(flightRepository.save(any())).thenReturn(flight)

        val updatedFlight = flightService.updateArrivalTime(scheduledFlight.flightNumber)

        assertEquals(updatedFlight.status, FlightStatus.ARRIVED)
        assertTrue(updatedFlight.arrivalTime!!.isBefore(scheduledFlight.estimatedArrivalTime))
    }

    @Test
    fun `update arrival time FAIL FlightNotFoundException`() {
        assertThrows<FlightNotFoundException> {
            flightService.updateArrivalTime("FR123")
        }
        verify(flightRepository, never()).save(any())
    }

    @Test
    fun `get all registered flight SUCCESS`() {
        `when`(flightRepository.findAll()).thenReturn(listOf())
        val flights = flightService.getAllRegisteredFlights()
        assertTrue(flights.isEmpty())
    }

    @Test
    fun `get flight status SUCCESS`() {
        assertThrows<FlightNotFoundException> {
            flightService.getFlightStatus("FR123")
        }
    }

    @Test
    fun `get flight status FAIL FlightNotFoundException`() {
        val scheduledFlight = FlightStub.scheduledFlightStub(status = FlightStatus.DEPARTED)
        val flight = FlightMapper.mapScheduledFlightToEntity(scheduledFlight)
        `when`(flightRepository.findFlightsByFlightNumberOrderByIdDesc(scheduledFlight.flightNumber)).thenReturn(flight)
        val flightStatus = flightService.getFlightStatus(scheduledFlight.flightNumber)
        assertEquals(flightStatus, FlightStatus.DEPARTED)
    }

    @Test
    fun `search flights by origin`() {
        val origin = "Vilnius VNO"
        flightService.searchFlights(origin, null)
        verify(flightRepository, times(1)).findFlightsByOrigin(origin)
    }

    @Test
    fun `search flights by destination`() {
        val destination = "Frankfurt FRA"
        flightService.searchFlights(null, destination)
        verify(flightRepository, times(1)).findFlightsByDestination(destination)
    }

    @Test
    fun `search flights by origin and by destination`() {
        val origin = "Vilnius VNO"
        val destination = "Frankfurt FRA"
        flightService.searchFlights(origin, destination)
        verify(flightRepository, times(1)).findFlightsByOriginAndDestination(origin, destination)
    }

    @Test
    fun `empty list return when no request params search flights`() {
        val flights = flightService.searchFlights(null, null)
        assertTrue(flights.isEmpty())
    }
}