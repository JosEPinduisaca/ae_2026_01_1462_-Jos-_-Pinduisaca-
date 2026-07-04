package com.pucetec.exam2.services

import com.pucetec.exam2.dto.EntryRequest
import com.pucetec.exam2.entities.ParkingSpace
import com.pucetec.exam2.entities.Ticket
import com.pucetec.exam2.exceptions.ParkingFullException
import com.pucetec.exam2.exceptions.SpaceAlreadyOccupiedException
import com.pucetec.exam2.exceptions.SpaceNotFoundException
import com.pucetec.exam2.exceptions.TicketAlreadyClosedException
import com.pucetec.exam2.exceptions.TicketNotFoundException
import com.pucetec.exam2.repositories.ParkingSpaceRepository
import com.pucetec.exam2.repositories.TicketRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ParkingServiceTest {

    @Mock
    private lateinit var spaceRepository: ParkingSpaceRepository

    @Mock
    private lateinit var ticketRepository: TicketRepository

    @InjectMocks
    private lateinit var parkingService: ParkingService

    private val availableSpace = ParkingSpace(id = 1L, code = "A1", status = "AVAILABLE")
    private val occupiedSpace = ParkingSpace(id = 2L, code = "A2", status = "OCCUPIED")

    // ─────────────────────────────────────────────
    // getAvailableSpaces
    // ─────────────────────────────────────────────

    @Test
    fun `getAvailableSpaces retorna lista de espacios disponibles`() {
        // Arrange
        Mockito.`when`(spaceRepository.findByStatus("AVAILABLE")).thenReturn(listOf(availableSpace))

        // Act
        val result = parkingService.getAvailableSpaces()

        // Assert
        Assertions.assertEquals(1, result.size)
        Assertions.assertEquals("A1", result[0].code)
    }

    // ─────────────────────────────────────────────
    // registerEntry
    // ─────────────────────────────────────────────

    @Test
    fun `registerEntry retorna TicketResponse cuando todo es valido`() {
        // Arrange
        val request = EntryRequest(plate = "ABC-123", spaceId = 1L)
        val occupiedNow = ParkingSpace(id = 1L, code = "A1", status = "OCCUPIED")
        val savedTicket = Ticket(id = 1L, plate = "ABC-123", entryTime = LocalDateTime.now(), exitTime = null, space = occupiedNow)

        Mockito.`when`(spaceRepository.findById(1L)).thenReturn(Optional.of(availableSpace))
        Mockito.`when`(ticketRepository.countByExitTimeIsNull()).thenReturn(0L)
        Mockito.`when`(spaceRepository.save(org.mockito.kotlin.any())).thenReturn(occupiedNow)
        Mockito.`when`(ticketRepository.save(org.mockito.kotlin.any())).thenReturn(savedTicket)

        // Act
        val response = parkingService.registerEntry(request)

        // Assert
        Assertions.assertEquals("ABC-123", response.plate)
        Assertions.assertEquals("OCCUPIED", response.space.status)
        Assertions.assertNull(response.exitTime)
    }

    @Test
    fun `registerEntry lanza SpaceNotFoundException cuando el espacio no existe`() {
        // Arrange
        val request = EntryRequest(plate = "ABC-123", spaceId = 99L)
        Mockito.`when`(spaceRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SpaceNotFoundException> {
            parkingService.registerEntry(request)
        }
    }

    @Test
    fun `registerEntry lanza SpaceAlreadyOccupiedException cuando el espacio ya esta ocupado`() {
        // Arrange
        val request = EntryRequest(plate = "ABC-123", spaceId = 2L)
        Mockito.`when`(spaceRepository.findById(2L)).thenReturn(Optional.of(occupiedSpace))

        // Act & Assert
        assertThrows<SpaceAlreadyOccupiedException> {
            parkingService.registerEntry(request)
        }
    }

    @Test
    fun `registerEntry lanza ParkingFullException cuando se alcanzo la capacidad maxima`() {
        // Arrange
        val request = EntryRequest(plate = "ABC-123", spaceId = 1L)
        Mockito.`when`(spaceRepository.findById(1L)).thenReturn(Optional.of(availableSpace))
        Mockito.`when`(ticketRepository.countByExitTimeIsNull()).thenReturn(20L)

        // Act & Assert
        assertThrows<ParkingFullException> {
            parkingService.registerEntry(request)
        }
    }

    // ─────────────────────────────────────────────
    // registerExit
    // ─────────────────────────────────────────────

    @Test
    fun `registerExit retorna TicketResponse con el espacio liberado`() {
        // Arrange
        val openTicket = Ticket(id = 1L, plate = "ABC-123", entryTime = LocalDateTime.now(), exitTime = null, space = occupiedSpace)
        val freedSpace = ParkingSpace(id = 2L, code = "A2", status = "AVAILABLE")
        val closedTicket = Ticket(id = 1L, plate = "ABC-123", entryTime = openTicket.entryTime, exitTime = LocalDateTime.now(), space = freedSpace)

        Mockito.`when`(ticketRepository.findById(1L)).thenReturn(Optional.of(openTicket))
        Mockito.`when`(spaceRepository.save(org.mockito.kotlin.any())).thenReturn(freedSpace)
        Mockito.`when`(ticketRepository.save(org.mockito.kotlin.any())).thenReturn(closedTicket)

        // Act
        val response = parkingService.registerExit(1L)

        // Assert
        Assertions.assertNotNull(response.exitTime)
        Assertions.assertEquals("AVAILABLE", response.space.status)
    }

    @Test
    fun `registerExit lanza TicketNotFoundException cuando el ticket no existe`() {
        // Arrange
        Mockito.`when`(ticketRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<TicketNotFoundException> {
            parkingService.registerExit(99L)
        }
    }

    @Test
    fun `registerExit lanza TicketAlreadyClosedException cuando el ticket ya esta cerrado`() {
        // Arrange
        val closedTicket = Ticket(id = 1L, plate = "ABC-123", entryTime = LocalDateTime.now().minusHours(2), exitTime = LocalDateTime.now(), space = occupiedSpace)
        Mockito.`when`(ticketRepository.findById(1L)).thenReturn(Optional.of(closedTicket))

        // Act & Assert
        assertThrows<TicketAlreadyClosedException> {
            parkingService.registerExit(1L)
        }
    }
}
