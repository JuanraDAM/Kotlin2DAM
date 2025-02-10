package com.example.proyectoevaluable.domain.cards.usecase

import com.example.proyectoevaluable.domain.cards.models.Card
import com.example.proyectoevaluable.domain.cards.repository.CardRepository

class UseCaseSaveCards(private val repository: CardRepository) {
    suspend operator fun invoke(cards: List<Card>) {
        repository.saveCards(cards)
    }
}
