package com.example.proyectoevaluable.ui.viewmodel.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoevaluable.domain.cards.models.Card
import com.example.proyectoevaluable.domain.cards.usecase.UseCaseLoadCards
import com.example.proyectoevaluable.domain.cards.usecase.UseCaseSaveCards
// import com.example.proyectoevaluable.domain.cards.usecase.UseCaseDeleteCard // Si decides usarlo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val loadCardsUseCase: UseCaseLoadCards,
    private val saveCardsUseCase: UseCaseSaveCards
    // private val deleteCardUseCase: UseCaseDeleteCard // Si lo deseas
) : ViewModel() {

    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>> get() = _cards

    fun loadCards() {
        viewModelScope.launch {
            _cards.value = loadCardsUseCase()
        }
    }

    fun saveCards(cards: List<Card>) {
        viewModelScope.launch {
            saveCardsUseCase(cards)
            _cards.value = cards
        }
    }

    fun deleteCard(position: Int) {
        val currentCards = _cards.value?.toMutableList() ?: return
        if (position in currentCards.indices) {
            currentCards.removeAt(position)
            saveCards(currentCards)
        }
        // Si usas el caso de uso, sería algo similar a:
        // viewModelScope.launch {
        //     _cards.value = deleteCardUseCase(currentCards, position)
        // }
    }
}
