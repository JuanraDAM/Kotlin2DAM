package com.example.proyectoevaluable.data.remote

import com.example.proyectoevaluable.domain.auth.AuthResponse
import com.example.proyectoevaluable.domain.auth.LoginRequest
import com.example.proyectoevaluable.domain.auth.RegisterRequest
import com.example.proyectoevaluable.domain.auth.RecoverPasswordRequest
import retrofit2.Response
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(email: String, password: String): Response<AuthResponse> {
        val request = LoginRequest(email, password)
        return apiService.loginUser(request)
    }

    suspend fun register(email: String, password: String): Response<String> {
        val request = RegisterRequest(email, password)
        return apiService.registerUser(request)
    }

    suspend fun logout(token: String): Response<Unit> {
        return apiService.logoutUser("Bearer $token")
    }

    suspend fun recoverPassword(email: String, newPassword: String): Response<Unit> {
        val request = RecoverPasswordRequest(email, newPassword)
        return apiService.recoverPassword(request)
    }
}
