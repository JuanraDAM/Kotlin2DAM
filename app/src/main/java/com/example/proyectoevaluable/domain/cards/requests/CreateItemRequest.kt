package com.example.proyectoevaluable.domain.cards.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateItemRequest(
    val title: String,
    val description: String?,
    val weight: Int,
    val image: String,
    val userId: Int
)
