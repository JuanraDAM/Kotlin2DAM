package com.example.srodenas.example_with_catalogs.domain.users.usecase

import com.example.srodenas.example_with_catalogs.domain.users.models.User
import com.example.srodenas.example_with_catalogs.repository.IUserRepository

class UseCaseLogin(private val userRepository: IUserRepository) {
    suspend fun login(email: String, password: String): User? {
        return userRepository.login(email, password)
    }
}
