package com.example.proyectoevaluable.data.cards.repository

import android.util.Log
import com.example.domain.Cards.CreateItemRequest
import com.example.proyectoevaluable.data.remote.ApiService
import com.example.proyectoevaluable.domain.cards.models.Card
import com.example.proyectoevaluable.domain.cards.models.Item
import com.example.proyectoevaluable.domain.cards.repository.CardRepository
import com.example.proyectoevaluable.domain.cards.requests.ItemsResponse
import com.example.domain.Cards.UpdateItemRequest as CardUpdateItemRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CardRepository {

    private val TAG = "CardRepository"

    override suspend fun createCard(card: Card): Result<Card> = withContext(Dispatchers.IO) {
        try {
            val request = CreateItemRequest(
                title = card.title,
                description = card.description,
                weight = card.weight,
                image = card.image ?: "",  // Se enviará la cadena Base64 (vacía si no hay imagen)
                userId = card.userId,
                latitude = card.latitude,    // <-- AGREGADO
                longitude = card.longitude   // <-- AGREGADO
            )
            Log.d(TAG, "createCard: Enviando CreateItemRequest: $request")
            val response: Response<Item> = apiService.createItem(request)
            Log.d(TAG, "createCard: Código de respuesta: ${response.code()}")
            if (response.isSuccessful) {
                response.body()?.let { createdItem ->
                    val createdCard = Card(
                        id = createdItem.id,
                        title = createdItem.title,
                        description = createdItem.description,
                        weight = createdItem.weight,
                        image = createdItem.image,
                        userId = createdItem.userId,
                        latitude = card.latitude,
                        longitude = card.longitude
                    )
                    Result.success(createdCard)
                } ?: Result.failure(Exception("Respuesta vacía"))
            } else {
                Result.failure(Exception("Error al crear ítem: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "createCard: Error", e)
            Result.failure(e)
        }
    }

    override suspend fun loadCards(): List<Card> = withContext(Dispatchers.IO) {
        val response: Response<ItemsResponse> = apiService.getItems()
        if (response.isSuccessful) response.body()?.items ?: emptyList() else emptyList()
    }

    override suspend fun updateCard(card: Card): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val request = CardUpdateItemRequest(
                title = card.title,
                description = card.description,
                weight = card.weight,
                image = card.image ?: "",
                latitude = card.latitude,    // <-- AGREGADO
                longitude = card.longitude   // <-- AGREGADO
            )
            Log.d(TAG, "Enviando UpdateItemRequest para card id=${card.id} con imagen de longitud: ${request.image!!.length}")
            card.id?.let { id ->
                val response = apiService.updateItem(id, request)
                Log.d(TAG, "Código de respuesta de updateItem: ${response.code()}")
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    val errorBody = response.errorBody()?.string()?.take(300)
                    Log.e(TAG, "Error al actualizar ítem: ${response.code()}, errorBody (recortado): $errorBody")
                    Result.failure(Exception("Error al actualizar ítem: ${response.code()}"))
                }
            } ?: Result.failure(Exception("ID nulo"))
        } catch (e: Exception) {
            Log.e(TAG, "Error en updateCard", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteCard(card: Card): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            card.id?.let { id ->
                val response = apiService.deleteItem(id)
                Log.d(TAG, "deleteCard: Código de delete: ${response.code()}")
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Log.e(TAG, "deleteCard: Error al eliminar ítem: ${response.code()}")
                    Result.failure(Exception("Error al eliminar ítem: ${response.code()}"))
                }
            } ?: Result.failure(Exception("ID nulo"))
        } catch (e: Exception) {
            Log.e(TAG, "deleteCard: Error", e)
            Result.failure(e)
        }
    }
}
