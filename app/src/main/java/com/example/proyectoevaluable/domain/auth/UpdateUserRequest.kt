package com.example.proyectoevaluable.domain.auth

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(val email: String, val password: String)
