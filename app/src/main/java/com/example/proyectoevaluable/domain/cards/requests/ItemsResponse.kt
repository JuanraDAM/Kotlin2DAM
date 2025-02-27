package com.example.proyectoevaluable.domain.cards.requests

import com.example.proyectoevaluable.domain.cards.models.Card
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class ItemsResponse(
    @Contextual
    val items: List<Card>
)
