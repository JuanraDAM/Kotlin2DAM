package com.example.proyectoevaluable.domain.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(val token: String, val userId: Int)
