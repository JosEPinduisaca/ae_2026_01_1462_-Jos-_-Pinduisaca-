package com.pucetec.exam2.repositories

import com.pucetec.exam2.entities.ParkingSpace
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParkingSpaceRepository : JpaRepository<ParkingSpace, Long> {
    // Usado por el endpoint publico: solo espacios disponibles
    fun findByStatus(status: String): List<ParkingSpace>
}
