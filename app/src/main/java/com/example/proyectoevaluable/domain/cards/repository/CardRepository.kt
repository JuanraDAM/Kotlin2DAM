package com.example.proyectoevaluable.domain.cards.repository

import com.example.proyectoevaluable.domain.cards.models.Card

interface CardRepository {
    suspend fun createCard(card: Card): Result<Card>
    suspend fun loadCards(): List<Card>
    suspend fun updateCard(card: Card): Result<Unit>
    suspend fun deleteCard(card: Card): Result<Unit>
}
