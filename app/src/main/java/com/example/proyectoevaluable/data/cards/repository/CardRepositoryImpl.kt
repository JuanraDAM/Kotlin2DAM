package com.example.proyectoevaluable.data.cards.repository

import com.example.proyectoevaluable.data.cards.datasource.SharedPrefsDataSource
import com.example.proyectoevaluable.domain.cards.models.Card
import com.example.proyectoevaluable.domain.cards.repository.CardRepository
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val dataSource: SharedPrefsDataSource
) : CardRepository {

    override suspend fun saveCards(cards: List<Card>) {
        dataSource.saveCards(cards)
    }

    override suspend fun loadCards(): List<Card> {
        return dataSource.loadCards()
    }

    override suspend fun deleteCard(card: Card) {
        // Cargamos la lista actual, eliminamos la tarjeta (usando equals de data class) y volvemos a guardar
        val currentCards = dataSource.loadCards().toMutableList()
        if (currentCards.remove(card)) {
            dataSource.saveCards(currentCards)
        }
    }
}
