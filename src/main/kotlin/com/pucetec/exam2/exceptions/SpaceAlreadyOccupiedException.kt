package com.pucetec.exam2.exceptions

// Validacion de negocio adicional: no permitir entrada en un espacio ya ocupado
class SpaceAlreadyOccupiedException(message: String? = null) : Exception(message)
