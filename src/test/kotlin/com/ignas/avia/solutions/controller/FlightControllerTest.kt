package com.ignas.avia.solutions.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ignas.avia.solutions.enums.FlightStatus
import com.ignas.avia.solutions.mapper.FlightMapper
import com.ignas.avia.solutions.repository.FlightRepository
import com.ignas.avia.solutions.stub.FlightStub
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.OffsetDateTime

@SpringBootTest
@AutoConfigureMockMvc
class FlightControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var flightRepository: FlightRepository

    @AfterEach
    fun purgeDatabase() {
        flightRepository.deleteAll()
    }

    @Test
    fun `register Scheduled flight SUCCESS`() {
        val scheduledFlight = FlightStub.scheduledFlightStub()
        mockMvc.post("/flights/register") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(scheduledFlight)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            jsonPath("$.flightNumber") { value(scheduledFlight.flightNumber) }
            jsonPath("$.aircraftType") { value(scheduledFlight.aircraftType) }
            jsonPath("$.origin") { value(scheduledFlight.origin) }
            jsonPath("$.destination") { value(scheduledFlight.destination) }
            jsonPath("$.estimatedDepartureTime") { isNotEmpty() }
            jsonPath("$.estimatedArrivalTime") { isNotEmpty() }
            jsonPath("$.status") { value(scheduledFlight.status.name) }
        }
    }

    @Test
    fun `register Scheduled flight FAIL IllegalDateTimeException`() {
        val scheduledFlight = FlightStub.scheduledFlightStub(estimatedDepartureTime = OffsetDateTime.now().plusDays(1))
        mockMvc.post("/flights/register") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(scheduledFlight)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.statusCode") { value(400) }
            jsonPath("$.message") { value("Estimated Departure Time cannot be after Estimated Arrival Time") }
        }
    }

    @Test
    fun `update departure time SUCESS`() {
        val scheduledFlight = FlightStub.scheduledFlightStub()
        val flightEntity = FlightMapper.mapScheduledFlightToEntity(scheduledFlight)
        val savedFlight = flightRepository.save(flightEntity)

        mockMvc.put("/flights/departure/{flightNumber}", scheduledFlight.flightNumber) {
        }.andExpect {
            status { isOk() }
            jsonPath("$.flightNumber") { value(savedFlight.flightNumber) }
            jsonPath("$.aircraftType") { value(savedFlight.aircraftType) }
            jsonPath("$.origin") { value(savedFlight.origin) }
            jsonPath("$.destination") { value(savedFlight.destination) }
            jsonPath("$.estimatedDepartureTime") { isEmpty() }
            jsonPath("$.departureTime") { isNotEmpty() }
            jsonPath("$.estimatedArrivalTime") { isNotEmpty() }
            jsonPath("$.arrivalTime") { isEmpty() }
            jsonPath("$.status") { value(FlightStatus.DEPARTED.name) }
        }
    }

    @Test
    fun `update departure time FAIL FlightNotFoundException`() {
        val flightNumber = "AAA123"
        mockMvc.put("/flights/departure/{flightNumber}", flightNumber) {
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.statusCode") { value(404) }
            jsonPath("$.message") { value("Scheduled flight number=[$flightNumber] was not found") }
        }
    }

    @Test
    fun `update arrival time SUCESS`() {
        val scheduledFlight = FlightStub.scheduledFlightStub(status = FlightStatus.DEPARTED)
        val flightEntity = FlightMapper.mapScheduledFlightToEntity(scheduledFlight)
        val savedFlight = flightRepository.save(flightEntity)

        mockMvc.put("/flights/arrival/{flightNumber}", scheduledFlight.flightNumber) {
        }.andExpect {
            status { isOk() }
            jsonPath("$.flightNumber") { value(savedFlight.flightNumber) }
            jsonPath("$.aircraftType") { value(savedFlight.aircraftType) }
            jsonPath("$.origin") { value(savedFlight.origin) }
            jsonPath("$.destination") { value(savedFlight.destination) }
            jsonPath("$.estimatedDepartureTime") { isEmpty() }
            jsonPath("$.departureTime") { isNotEmpty() }
            jsonPath("$.estimatedArrivalTime") { isEmpty() }
            jsonPath("$.arrivalTime") { isNotEmpty() }
            jsonPath("$.status") { value(FlightStatus.ARRIVED.name) }
        }
    }

    @Test
    fun `update arrival time FAIL FlightNotFoundException`() {
        val flightNumber = "AAA123"
        mockMvc.put("/flights/arrival/{flightNumber}", flightNumber) {
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.statusCode") { value(404) }
            jsonPath("$.message") { value("Departed flight number=[$flightNumber] was not found") }
        }
    }

    @Test
    fun `get all flights`() {
        val scheduledFlight = FlightStub.scheduledFlightStub()
        val flightEntity = FlightMapper.mapScheduledFlightToEntity(scheduledFlight)
        flightRepository.save(flightEntity)
        mockMvc.get("/flights")
            .andExpect {
                status { isOk() }
                jsonPath("$.length()") { value(1) }
            }
    }

    @Test
    fun `get flight status SUCCESS`() {
        val scheduledFlight = FlightStub.scheduledFlightStub()
        val flightEntity = FlightMapper.mapScheduledFlightToEntity(scheduledFlight)
        flightRepository.save(flightEntity)
        mockMvc.get("/flights/status/{flightNumber}", scheduledFlight.flightNumber)
            .andExpect {
                status { isOk() }
                jsonPath("$") { value(FlightStatus.SCHEDULED.name) }
            }
    }

    @Test
    fun `get flight status FAIL FlightNotFoundException`() {
        mockMvc.get("/flights/status/{flightNumber}", "AAA123")
            .andExpect {
                status { isNotFound() }
                jsonPath("$.statusCode") { value(404) }
                jsonPath("$.message") { value("Flight number=[AAA123] was not found") }
            }
    }

    @Test
    fun `search flights by origin`() {
        val scheduledFlight = FlightStub.scheduledFlightStub()
        val flightEntity = FlightMapper.mapScheduledFlightToEntity(scheduledFlight)
        flightRepository.save(flightEntity)

        mockMvc.get("/flights/search") {
            param("origin", flightEntity.origin)
        }.andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(1) }
        }
    }

    @Test
    fun `search flights by destination`() {
        val scheduledFlight = FlightStub.scheduledFlightStub()
        val flightEntity = FlightMapper.mapScheduledFlightToEntity(scheduledFlight)
        flightRepository.save(flightEntity)

        mockMvc.get("/flights/search") {
            param("destination", flightEntity.destination)
        }.andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(1) }
        }
    }

    @Test
    fun `search flights by origin and by destination`() {
        val scheduledFlight = FlightStub.scheduledFlightStub()
        val flightEntity = FlightMapper.mapScheduledFlightToEntity(scheduledFlight)
        flightRepository.save(flightEntity)

        mockMvc.get("/flights/search") {
            param("origin", flightEntity.origin)
            param("destination", flightEntity.destination)
        }.andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(1) }
        }
    }

    @Test
    fun `empty list return when no request params search flights`() {
        val scheduledFlight = FlightStub.scheduledFlightStub()
        val flightEntity = FlightMapper.mapScheduledFlightToEntity(scheduledFlight)
        flightRepository.save(flightEntity)

        mockMvc.get("/flights/search") {
        }.andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(0) }
        }
    }
}