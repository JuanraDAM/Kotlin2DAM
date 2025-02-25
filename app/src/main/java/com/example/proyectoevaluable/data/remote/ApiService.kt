package com.example.proyectoevaluable.data.remote

import com.example.proyectoevaluable.domain.auth.AuthResponse
import com.example.proyectoevaluable.domain.auth.LoginRequest
import com.example.proyectoevaluable.domain.auth.RegisterRequest
import com.example.proyectoevaluable.domain.auth.RecoverPasswordRequest
import com.example.proyectoevaluable.domain.auth.UpdateUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Header

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
}
