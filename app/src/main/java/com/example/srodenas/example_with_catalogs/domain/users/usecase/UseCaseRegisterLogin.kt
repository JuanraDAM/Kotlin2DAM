package com.example.srodenas.example_with_catalogs.domain.users.usecase

import com.example.srodenas.example_with_catalogs.domain.users.models.User
import com.example.srodenas.example_with_catalogs.repository.IUserRepository

class UseCaseRegisterLogin(private val userRepository: IUserRepository) {
    suspend fun register(user: User): Boolean {
        return userRepository.registrarUsuario(user)
    }
}
