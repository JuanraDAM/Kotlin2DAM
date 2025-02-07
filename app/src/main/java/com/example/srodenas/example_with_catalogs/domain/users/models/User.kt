package com.example.srodenas.example_with_catalogs.domain.users.models

/*
Modelo para el usuario registrado
 */
data class User(
    var id: Int,
    val name: String,
    val email: String,
    val password: String,  // Consistente con UserEntity
    val phone: String,
    val imag: String       // Consistente con UserEntity
){

    //constructor primario
    constructor(email: String, passw: String):
            this(0, "", email, passw, "", "")

}
