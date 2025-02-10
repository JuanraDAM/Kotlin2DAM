package com.example.proyectoevaluable.domain.cards.models

data class Card(
    var username: String,
    var password: String,
    var weight: String? = null,
    var photoUri: String? = null
)
