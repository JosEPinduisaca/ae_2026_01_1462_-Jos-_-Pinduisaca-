package com.pucetec.exam2.services

import com.pucetec.exam2.dto.EntryRequest
import com.pucetec.exam2.dto.ParkingSpaceResponse
import com.pucetec.exam2.dto.TicketResponse
import com.pucetec.exam2.entities.ParkingSpace
import com.pucetec.exam2.entities.Ticket
import com.pucetec.exam2.exceptions.ParkingFullException
import com.pucetec.exam2.exceptions.SpaceAlreadyOccupiedException
import com.pucetec.exam2.exceptions.SpaceNotFoundException
import com.pucetec.exam2.exceptions.TicketAlreadyClosedException
import com.pucetec.exam2.exceptions.TicketNotFoundException
import com.pucetec.exam2.mappers.toResponse
import com.pucetec.exam2.repositories.ParkingSpaceRepository
import com.pucetec.exam2.repositories.TicketRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

private const val AVAILABLE = "AVAILABLE"
private const val OCCUPIED = "OCCUPIED"

@Service
class ParkingService(
    private val spaceRepository: ParkingSpaceRepository,
    private val ticketRepository: TicketRepository
) {
    private val logger = LoggerFactory.getLogger(ParkingService::class.java)

    // Capacidad maxima del estacionamiento: unico lugar donde se define este valor.
    private val capacidad = 20

    fun getAvailableSpaces(): List<ParkingSpaceResponse> {
        logger.info("Getting available parking spaces")
        return spaceRepository.findByStatus(AVAILABLE).map { it.toResponse() }
    }

    fun registerEntry(request: EntryRequest): TicketResponse {
        logger.info("Registering entry for plate ${request.plate} at space ${request.spaceId}")

        val space = spaceRepository.findById(request.spaceId)
            .orElseThrow { SpaceNotFoundException("Espacio no encontrado con ID: ${request.spaceId}") }

        if (space.status == OCCUPIED) {
            throw SpaceAlreadyOccupiedException("El espacio con ID: ${request.spaceId} ya esta ocupado")
        }

        val activeTickets = ticketRepository.countByExitTimeIsNull()
        if (activeTickets >= capacidad) {
            throw ParkingFullException("El estacionamiento alcanzo su capacidad maxima de $capacidad espacios")
        }

        val updatedSpace = ParkingSpace(
            id = space.id,
            code = space.code,
            status = OCCUPIED
        )
        val savedSpace = spaceRepository.save(updatedSpace)

        val ticketToSave = Ticket(
            plate = request.plate,
            entryTime = LocalDateTime.now(),
            exitTime = null,
            space = savedSpace
        )

        val savedTicket = ticketRepository.save(ticketToSave)
        logger.info("Entry registered, ticket id ${savedTicket.id}")
        return savedTicket.toResponse()
    }

    fun registerExit(ticketId: Long): TicketResponse {
        logger.info("Registering exit for ticket $ticketId")

        val ticket = ticketRepository.findById(ticketId)
            .orElseThrow { TicketNotFoundException("Ticket no encontrado con ID: $ticketId") }

        if (ticket.exitTime != null) {
            throw TicketAlreadyClosedException("El ticket con ID: $ticketId ya esta cerrado")
        }

        val updatedSpace = ParkingSpace(
            id = ticket.space.id,
            code = ticket.space.code,
            status = AVAILABLE
        )
        val savedSpace = spaceRepository.save(updatedSpace)

        val updatedTicket = Ticket(
            id = ticket.id,
            plate = ticket.plate,
            entryTime = ticket.entryTime,
            exitTime = LocalDateTime.now(),
            space = savedSpace
        )

        val savedTicket = ticketRepository.save(updatedTicket)
        logger.info("Exit registered for ticket ${savedTicket.id}")
        return savedTicket.toResponse()
    }
}
