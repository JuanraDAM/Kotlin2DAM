package com.example.proyectoevaluable.domain.cards.models

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val id: Int? = null, // Será asignado por el servidor
    val title: String,
    val description: String?,
    val weight: Int,
    val image: String?,   // Puede ser una URL o cadena Base64
    val userId: Int,
    val latitude: Double? = null,
    val longitude: Double? = null
)
