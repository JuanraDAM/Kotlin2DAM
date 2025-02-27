package com.example.proyectoevaluable.data.remote

import com.example.domain.models.Item
import com.example.proyectoevaluable.domain.auth.AuthResponse
import com.example.proyectoevaluable.domain.auth.LoginRequest
import com.example.proyectoevaluable.domain.auth.RegisterRequest
import com.example.proyectoevaluable.domain.auth.RecoverPasswordRequest
import com.example.proyectoevaluable.domain.auth.UpdateUserRequest
import com.example.proyectoevaluable.domain.cards.models.Card
import com.example.proyectoevaluable.domain.cards.requests.CreateItemRequest
import com.example.proyectoevaluable.domain.cards.requests.CreateItemResponse
import com.example.proyectoevaluable.domain.cards.requests.ItemsResponse
import com.example.proyectoevaluable.domain.cards.requests.UpdateItemRequest as CardUpdateItemRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<String>

    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/logout")
    suspend fun logoutUser(@Header("Authorization") token: String): Response<Unit>

    @POST("auth/recover")
    suspend fun recoverPassword(@Body request: RecoverPasswordRequest): Response<Unit>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body request: UpdateUserRequest
    ): Response<String>

    @POST("items")
    suspend fun createItem(@Body request: CreateItemRequest): Response<Item>


    @GET("items")
    suspend fun getItems(): Response<ItemsResponse>

    @PUT("items/{id}")
    suspend fun updateItem(@Path("id") id: Int, @Body request: CardUpdateItemRequest): Response<String>

    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: Int): Response<String>
}
