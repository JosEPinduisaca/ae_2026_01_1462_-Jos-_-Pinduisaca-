package com.pucetec.exam2.dto

data class EntryRequest(
    val plate: String,
    val spaceId: Long
)

data class TicketResponse(
    val id: Long,
    val plate: String,
    val entryTime: String,
    val exitTime: String?,
    val space: ParkingSpaceResponse
)
