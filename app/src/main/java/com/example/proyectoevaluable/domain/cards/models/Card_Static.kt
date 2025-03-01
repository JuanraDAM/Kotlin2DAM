package com.example.proyectoevaluable.domain.cards.models

import kotlinx.serialization.Serializable

@Serializable
data class Card_Static(
    val id: Int? = null,
    val title: String,
    val description: String?,
    val image: String?,
    val userId: Int,
)
