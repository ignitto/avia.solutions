package com.ignas.avia.solutions.controller

import com.ignas.avia.solutions.entity.Flight
import com.ignas.avia.solutions.enums.FlightStatus
import com.ignas.avia.solutions.model.FlightDto
import com.ignas.avia.solutions.model.ScheduledFlight
import com.ignas.avia.solutions.service.FlightService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/flights")
class FlightController(
    val flightService: FlightService
) {

    @GetMapping
    fun getAllFlights(): ResponseEntity<List<FlightDto>> {
        val flights = flightService.getAllRegisteredFlights()
        return ResponseEntity(flights, HttpStatus.OK)
    }

    @GetMapping("/search")
    fun searchFlights(@RequestParam(required = false) origin: String?,
                      @RequestParam(required = false) destination: String?
    ): ResponseEntity<List<FlightDto>> {
        val flights = flightService.searchFlights(origin, destination)
        return ResponseEntity(flights, HttpStatus.OK)
    }

    @GetMapping("/status/{flightNumber}")
    fun getFlightStatus(@PathVariable flightNumber: String): ResponseEntity<FlightStatus> {
        val flightStatus = flightService.getFlightStatus(flightNumber)
        return ResponseEntity(flightStatus, HttpStatus.OK)
    }

    @PostMapping("/register")
    fun registerScheduledFlight(@RequestBody scheduledFlight: ScheduledFlight): ResponseEntity<ScheduledFlight> {
        val registeredFlight = flightService.registerScheduledFlight(scheduledFlight)
        return ResponseEntity(registeredFlight, HttpStatus.CREATED)
    }

    @PutMapping("/departure/{flightNumber}")
    fun updateDeparture(@PathVariable flightNumber: String): ResponseEntity<FlightDto> {
        val updatedDepartureTime = flightService.updateDepartureTime(flightNumber)
        return ResponseEntity(updatedDepartureTime, HttpStatus.OK)
    }

    @PutMapping("/arrival/{flightNumber}")
    fun updateArrivalTime(@PathVariable flightNumber: String): ResponseEntity<FlightDto> {
        val updatedArrivalTime = flightService.updateArrivalTime(flightNumber)
        return ResponseEntity(updatedArrivalTime, HttpStatus.OK)
    }
}