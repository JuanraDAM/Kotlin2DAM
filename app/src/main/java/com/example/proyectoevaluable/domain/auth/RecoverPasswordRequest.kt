package com.example.proyectoevaluable.domain.auth

import kotlinx.serialization.Serializable

@Serializable
data class RecoverPasswordRequest(val email: String, val newPassword: String)
