package com.example.proyectoevaluable.domain.cards.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateItemRequest(
    val title: String,
    val description: String? = null,
    val weight: Int,
    val image: String,  // La imagen se recibirá como cadena Base64
    val userId: Int
)
