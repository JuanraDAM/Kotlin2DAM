package com.example.proyectoevaluable.domain.cards.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateItemRequest(
    val title: String?,
    val description: String?,
    val weight: Int?,
    val image: String?
)
