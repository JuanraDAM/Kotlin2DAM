package com.example.proyectoevaluable.domain.cards.usecase

import com.example.proyectoevaluable.domain.cards.models.Card
import com.example.proyectoevaluable.domain.cards.repository.CardRepository

class UseCaseDeleteCard(private val repository: CardRepository) {
    suspend operator fun invoke(cards: List<Card>, position: Int): List<Card> {
        val mutableCards = cards.toMutableList()
        if (position in mutableCards.indices) {
            mutableCards.removeAt(position)
            repository.saveCards(mutableCards)
        }
        return mutableCards
    }
}
