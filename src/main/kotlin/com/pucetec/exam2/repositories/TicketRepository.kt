package com.pucetec.exam2.repositories

import com.pucetec.exam2.entities.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<Ticket, Long> {
    // Cuenta los tickets abiertos (sin exitTime) = vehiculos actualmente adentro
    fun countByExitTimeIsNull(): Long
}
