package com.pucetec.exam2.controllers

import com.pucetec.exam2.dto.EntryRequest
import com.pucetec.exam2.dto.TicketResponse
import com.pucetec.exam2.services.ParkingService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class TicketController(
    val parkingService: ParkingService
) {
    private val logger = LoggerFactory.getLogger(TicketController::class.java)

    // Privado: requiere token de Cognito
    @PostMapping("/api/tickets/entry")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerEntry(@RequestBody request: EntryRequest): TicketResponse {
        logger.info("Registering entry")
        return parkingService.registerEntry(request)
    }

    // Privado: requiere token de Cognito
    @PutMapping("/api/tickets/{id}/exit")
    fun registerExit(@PathVariable id: Long): TicketResponse {
        logger.info("Registering exit for ticket $id")
        return parkingService.registerExit(id)
    }
}
