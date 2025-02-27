package com.example.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Int,
    val title: String,
    val description: String? = null,
    val weight: Int,
    val image: String,
    val userId: Int
)
