package com.pucetec.exam2.exceptions

// Validacion de negocio adicional: no permitir cerrar un ticket ya cerrado
class TicketAlreadyClosedException(message: String? = null) : Exception(message)
