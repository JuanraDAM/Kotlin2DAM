package com.example.proyectoevaluable.ui.viewmodel.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.proyectoevaluable.domain.cards.models.Card
import com.example.proyectoevaluable.domain.cards.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _createStatus = MutableLiveData<Boolean>()
    val createStatus: LiveData<Boolean> get() = _createStatus

    // Exponemos la lista de cards para la UI
    private val _cards = MutableLiveData<List<Card>>(emptyList())
    val cards: LiveData<List<Card>> get() = _cards

    // Método para guardar una nueva card
    fun saveCard(card: Card) {
        viewModelScope.launch {
            val result = cardRepository.createCard(card)
            if (result.isSuccess) {
                val createdCard = result.getOrNull()
                val currentList = _cards.value.orEmpty()
                _cards.postValue(currentList + listOf(createdCard!!))
            } else {
                // Aquí puedes gestionar el error, por ejemplo, mostrar un Toast o registrar el error
            }
        }
    }

    // Métodos de actualización, eliminación y carga de cards (implementa según tu lógica)
    fun updateCard(card: Card) { /* ... */ }
    fun deleteCard(position: Int) { /* ... */ }
    fun loadCards() { /* ... */ }
}
