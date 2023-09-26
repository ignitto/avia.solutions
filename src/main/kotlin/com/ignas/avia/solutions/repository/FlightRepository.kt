package com.ignas.avia.solutions.repository

import com.ignas.avia.solutions.entity.Flight
import com.ignas.avia.solutions.enums.FlightStatus
import org.springframework.data.jpa.repository.JpaRepository

interface FlightRepository : JpaRepository<Flight, Long> {

    fun findFlightsByFlightNumberAndStatus(flightNumber: String, status: FlightStatus): Flight?

    fun findFlightsByOriginAndDestination(origin: String, destination: String): List<Flight>

    fun findFlightsByOrigin(origin: String): List<Flight>

    fun findFlightsByDestination(destination: String): List<Flight>

    fun findFlightsByFlightNumberOrderByIdDesc(flightNumber: String): Flight?


}