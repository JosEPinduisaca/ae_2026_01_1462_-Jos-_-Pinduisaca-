package com.pucetec.exam2.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "tickets")
class Ticket(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val plate: String = "",

    @Column(name = "entry_time")
    val entryTime: LocalDateTime = LocalDateTime.now(),

    // Null mientras el vehiculo sigue adentro
    @Column(name = "exit_time")
    val exitTime: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    val space: ParkingSpace
)
