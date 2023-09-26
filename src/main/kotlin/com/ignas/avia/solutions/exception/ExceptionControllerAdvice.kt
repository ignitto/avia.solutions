package com.ignas.avia.solutions.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler
    fun handleFlightNotFoundException(ex: FlightNotFoundException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.message!!)
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleIllegalDateTimeException(ex: IllegalDateTimeException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.message!!)
        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }
}

data class ErrorMessage(val statusCode: Int, val message: String)