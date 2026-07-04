package com.pucetec.exam2.controllers

import com.pucetec.exam2.dto.ParkingSpaceResponse
import com.pucetec.exam2.services.ParkingService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ParkingSpaceController(
    val parkingService: ParkingService
) {
    private val logger = LoggerFactory.getLogger(ParkingSpaceController::class.java)

    // Publico: cualquiera puede ver los espacios disponibles
    @GetMapping("/api/spaces/available")
    fun getAvailableSpaces(): List<ParkingSpaceResponse> {
        logger.info("Getting available spaces")
        return parkingService.getAvailableSpaces()
    }
}
