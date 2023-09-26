package com.ignas.avia.solutions

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlightRegistrationSystemApplication

fun main(args: Array<String>) {
	runApplication<FlightRegistrationSystemApplication>(*args)
}
