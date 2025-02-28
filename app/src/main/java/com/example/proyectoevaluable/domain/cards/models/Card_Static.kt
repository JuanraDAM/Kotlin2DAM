package com.example.proyectoevaluable.domain.cards.models

import kotlinx.serialization.Serializable

@Serializable
data class Card_Static(
    val id: Int? = null, // Será asignado por el servidor
    val title: String,
    val description: String?,
    val image: String?,   // Puede ser una URL o cadena Base64
    val userId: Int,
)
