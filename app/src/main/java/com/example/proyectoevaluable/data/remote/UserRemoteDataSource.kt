package com.example.proyectoevaluable.data.remote

import com.example.proyectoevaluable.domain.auth.UpdateUserRequest
import retrofit2.Response
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun updateUser(id: Int, request: UpdateUserRequest): Response<String> {
        return apiService.updateUser(id, request)
    }
}
