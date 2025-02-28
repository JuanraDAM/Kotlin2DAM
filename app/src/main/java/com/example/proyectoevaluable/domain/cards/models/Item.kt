package com.example.proyectoevaluable.domain.cards.models

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Int,
    val title: String,
    val description: String? = null,
    val weight: Int,
    val image: String,
    val userId: Int,
    val latitude: Double? = null,
    val longitude: Double? = null
)
