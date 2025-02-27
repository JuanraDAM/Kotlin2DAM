package com.example.proyectoevaluable.domain.cards.usecase

import com.example.proyectoevaluable.domain.cards.models.Card
import com.example.proyectoevaluable.domain.cards.repository.CardRepository
import javax.inject.Inject

class UseCaseSaveCards @Inject constructor(
    private val repository: CardRepository
) {
    suspend operator fun invoke(card: Card): Boolean {
        val result = repository.createCard(card)
        return result.isSuccess
    }
}
