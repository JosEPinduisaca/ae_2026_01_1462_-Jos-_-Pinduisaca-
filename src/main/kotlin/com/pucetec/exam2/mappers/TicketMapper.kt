package com.pucetec.exam2.mappers

import com.pucetec.exam2.dto.TicketResponse
import com.pucetec.exam2.entities.Ticket

fun Ticket.toResponse() = TicketResponse(
    id = this.id,
    plate = this.plate,
    entryTime = this.entryTime.toString(),
    exitTime = this.exitTime?.toString(),
    space = this.space.toResponse()
)
