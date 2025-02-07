package com.example.srodenas.example_with_catalogs.domain.users.models

/*
Modelo para el usuario registrado
 */
data class User(
    var id: Int,
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val imag: String
){

    constructor(email: String, passw: String):
            this(0, "", email, passw, "", "")

}
