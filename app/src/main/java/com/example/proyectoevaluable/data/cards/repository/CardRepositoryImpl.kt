package com.example.proyectoevaluable.data.cards.repository

import android.util.Log
import com.example.domain.models.Item
import com.example.proyectoevaluable.data.remote.ApiService
import com.example.proyectoevaluable.domain.cards.models.Card
import com.example.proyectoevaluable.domain.cards.repository.CardRepository
import com.example.proyectoevaluable.domain.cards.requests.CreateItemRequest
import com.example.proyectoevaluable.domain.cards.requests.ItemsResponse
import com.example.proyectoevaluable.domain.cards.requests.UpdateItemRequest as CardUpdateItemRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CardRepository {

    override suspend fun createCard(card: Card): Result<Card> = withContext(Dispatchers.IO) {
        try {
            // Mapear Card a CreateItemRequest
            val request = CreateItemRequest(
                title = card.title,
                description = card.description,
                weight = card.weight,
                image = card.image ?: "",
                userId = card.userId
            )
            Log.d("CardRepository", "Enviando CreateItemRequest: $request")
            val response: Response<Item> = apiService.createItem(request)
            Log.d("CardRepository", "Código de respuesta: ${response.code()}")
            Log.d("CardRepository", "Respuesta de createItem: ${response.body()}")
            if (response.isSuccessful) {
                val createdItem = response.body()
                if (createdItem != null) {
                    // Convertir Item a Card
                    val createdCard = Card(
                        id = createdItem.id,
                        title = createdItem.title,
                        description = createdItem.description,
                        weight = createdItem.weight,
                        image = createdItem.image,
                        userId = createdItem.userId,
                        latitude = card.latitude,   // Puedes ajustar según corresponda
                        longitude = card.longitude  // Puedes ajustar según corresponda
                    )
                    Result.success(createdCard)
                } else {
                    Result.failure(Exception("Respuesta vacía"))
                }
            } else {
                Result.failure(Exception("Error al crear ítem: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("CardRepository", "Error en createCard", e)
            Result.failure(e)
        }
    }

    override suspend fun loadCards(): List<Card> = withContext(Dispatchers.IO) {
        val response: Response<ItemsResponse> = apiService.getItems()
        if (response.isSuccessful) {
            response.body()?.items ?: emptyList()
        } else {
            emptyList()
        }
    }

    override suspend fun updateCard(card: Card): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val request = CardUpdateItemRequest(
                title = card.title,
                description = card.description,
                weight = card.weight,
                image = card.image ?: ""
            )
            card.id?.let { id ->
                val response = apiService.updateItem(id, request)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Error al actualizar ítem: ${response.code()}"))
                }
            } ?: Result.failure(Exception("ID nulo"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCard(card: Card): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            card.id?.let { id ->
                val response = apiService.deleteItem(id)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Error al eliminar ítem: ${response.code()}"))
                }
            } ?: Result.failure(Exception("ID nulo"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
