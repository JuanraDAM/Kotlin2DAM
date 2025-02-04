package com.example.proyectoevaluable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel() {

    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>> get() = _cards

    fun loadCards() {
        viewModelScope.launch {
            _cards.value = repository.loadCardsData()
        }
    }

    fun saveCards(cards: List<Card>) {
        viewModelScope.launch {
            repository.saveCardsData(cards)
            _cards.value = cards
        }
    }

    fun deleteCard(position: Int) {
        val currentCards = _cards.value?.toMutableList() ?: return
        if (position >= 0 && position < currentCards.size) {
            currentCards.removeAt(position)
            saveCards(currentCards)
        }
    }
}