package com.pucetec.exam2.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(SpaceNotFoundException::class)
    fun handleSpaceNotFoundException(e: SpaceNotFoundException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Espacio no encontrado - ERROR",
            source = "ParkingService"
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }

    @ExceptionHandler(TicketNotFoundException::class)
    fun handleTicketNotFoundException(e: TicketNotFoundException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Ticket no encontrado - ERROR",
            source = "ParkingService"
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }

    @ExceptionHandler(ParkingFullException::class)
    fun handleParkingFullException(e: ParkingFullException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Estacionamiento lleno - ERROR",
            source = "ParkingService"
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }

    @ExceptionHandler(SpaceAlreadyOccupiedException::class)
    fun handleSpaceAlreadyOccupiedException(e: SpaceAlreadyOccupiedException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "El espacio ya esta ocupado - ERROR",
            source = "ParkingService"
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }

    @ExceptionHandler(TicketAlreadyClosedException::class)
    fun handleTicketAlreadyClosedException(e: TicketAlreadyClosedException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "El ticket ya esta cerrado - ERROR",
            source = "ParkingService"
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }
}

data class ExceptionResponse(
    val message: String,
    val source: String
)
