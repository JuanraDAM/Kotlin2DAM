package com.example.proyectoevaluable.data.auth

import com.example.proyectoevaluable.data.remote.UserRemoteDataSource
import com.example.proyectoevaluable.domain.auth.UpdateUserRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) {
    suspend fun updateUser(id: Int, email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val request = UpdateUserRequest(email, password)
            val response = remoteDataSource.updateUser(id, request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                // Puedes agregar un log para imprimir response.errorBody()?.string()
                Result.failure(Exception("Error al actualizar: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
