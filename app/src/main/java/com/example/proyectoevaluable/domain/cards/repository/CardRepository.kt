package com.example.proyectoevaluable.domain.cards.repository

import com.example.proyectoevaluable.domain.cards.models.Card

interface CardRepository {
    suspend fun saveCards(cards: List<Card>)
    suspend fun loadCards(): List<Card>
    suspend fun deleteCard(card: Card)
}
