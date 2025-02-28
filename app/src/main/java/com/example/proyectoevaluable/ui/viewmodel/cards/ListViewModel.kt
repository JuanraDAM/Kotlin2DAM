package com.example.proyectoevaluable.ui.viewmodel.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.proyectoevaluable.domain.cards.models.Card
import com.example.proyectoevaluable.domain.cards.repository.CardRepository
import com.example.proyectoevaluable.domain.cards.usecase.UseCaseDeleteCard
import com.example.proyectoevaluable.di.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val deleteCardUseCase: com.example.proyectoevaluable.domain.cards.usecase.UseCaseDeleteCard,
    private val tokenManager: TokenManager
) : ViewModel() {

    // LiveData que contiene la lista de cards
    private val _cards = MutableLiveData<List<Card>>(emptyList())
    val cards: LiveData<List<Card>> get() = _cards

    // Guarda una nueva card y la añade a la lista
    fun saveCard(card: Card) {
        viewModelScope.launch {
            val result = cardRepository.createCard(card)
            if (result.isSuccess) {
                val createdCard = result.getOrNull()
                val currentList = _cards.value.orEmpty()
                _cards.postValue(currentList + listOf(createdCard!!))
            } else {
                // Aquí podrías notificar el error a la UI
            }
        }
    }

    // Elimina una card y recarga la lista filtrada por el usuario actual
    fun deleteCard(position: Int) {
        viewModelScope.launch {
            val currentCards = _cards.value.orEmpty().toMutableList()
            if (position in currentCards.indices) {
                val card = currentCards[position]
                val result = deleteCardUseCase.invoke(card)
                if (result) {
                    // Recarga las cards del usuario tras la eliminación
                    loadCards()
                } else {
                    // Gestión del error: podrías notificar a la UI, por ejemplo con un Toast
                }
            }
        }
    }

    // Actualiza una card y actualiza la lista localmente
    fun updateCard(card: Card) {
        viewModelScope.launch {
            val result = cardRepository.updateCard(card)
            if (result.isSuccess) {
                // Actualiza la card en la lista local
                val currentCards = _cards.value.orEmpty().toMutableList()
                val index = currentCards.indexOfFirst { it.id == card.id }
                if (index != -1) {
                    currentCards[index] = card
                    _cards.postValue(currentCards)
                }
            } else {
                // Gestiona el error (mostrar mensaje, etc.)
            }
        }
    }

    // Carga las cards filtradas por el userId actual (obtenido de TokenManager)
    fun loadCards() {
        viewModelScope.launch {
            val allCards = cardRepository.loadCards()
            val currentUserId = tokenManager.getUserId() ?: -1
            // Filtra para mostrar solo las cards del usuario actual
            _cards.postValue(allCards.filter { it.userId == currentUserId })
        }
    }
}
