package com.example.proyectoevaluable.domain.auth

import com.example.proyectoevaluable.data.auth.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: Int, email: String, password: String): Boolean {
        return userRepository.updateUser(id, email, password).isSuccess
    }
}
