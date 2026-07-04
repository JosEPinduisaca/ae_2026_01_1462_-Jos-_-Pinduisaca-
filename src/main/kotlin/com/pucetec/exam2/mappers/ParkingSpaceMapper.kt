package com.pucetec.exam2.mappers

import com.pucetec.exam2.dto.ParkingSpaceResponse
import com.pucetec.exam2.entities.ParkingSpace

fun ParkingSpace.toResponse() = ParkingSpaceResponse(
    id = this.id,
    code = this.code,
    status = this.status
)
