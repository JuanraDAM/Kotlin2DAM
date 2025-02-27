package com.example.proyectoevaluable.data.auth

import com.example.proyectoevaluable.data.remote.AuthRemoteDataSource
import com.example.proyectoevaluable.di.TokenManager
import com.example.proyectoevaluable.domain.auth.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokenManager: TokenManager
) {

    suspend fun login(email: String, password: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = remoteDataSource.login(email, password)
            Log.d("AuthRepository", "Login response code: ${response.code()}")
            Log.d("AuthRepository", "Login response body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.let { authResponse: AuthResponse ->
                    tokenManager.saveToken(authResponse.token)
                    tokenManager.saveUserEmail(email)
                    tokenManager.saveUserId(authResponse.userId)
                    Log.d("AuthRepository", "Token guardado: ${authResponse.token}")
                    Result.success(authResponse.token)
                } ?: Result.failure(Exception("Respuesta vacía"))
            } else {
                Result.failure(Exception("Error en login: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error en login", e)
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = remoteDataSource.register(email, password)
            if (response.isSuccessful) {
                Result.success(response.body() ?: "Registro exitoso")
            } else {
                Result.failure(Exception("Error en registro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            tokenManager.getToken()?.let { token ->
                val response = remoteDataSource.logout(token)
                if (response.isSuccessful) {
                    tokenManager.clearToken()
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Error en logout: ${response.code()}"))
                }
            } ?: Result.failure(Exception("No hay token almacenado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Método para recuperación de contraseña
    suspend fun recoverPassword(email: String, newPassword: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = remoteDataSource.recoverPassword(email, newPassword)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error en recuperación: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
