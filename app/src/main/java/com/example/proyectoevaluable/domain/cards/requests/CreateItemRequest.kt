package com.example.domain.Cards

import kotlinx.serialization.Serializable

@Serializable
data class CreateItemRequest(
    val title: String,
    val description: String? = null,
    val weight: Int,
    val image: String,  // La imagen se recibirá como cadena Base64
    val userId: Int,
    // Nuevos campos para ubicación:
    val latitude: Double? = null,
    val longitude: Double? = null
)
