package com.example.srodenas.example_with_catalogs.repository

import com.example.srodenas.example_with_catalogs.domain.users.models.User

interface IUserRepository {
    suspend fun registrarUsuario(usuario: User): Boolean
    suspend fun login(email: String, passw: String): User?
    suspend fun eliminarUsuarioPorId(id: Int): Boolean
    suspend fun actualizarNombre(id: Int, nuevoNombre: String): Boolean
    suspend fun getAllUsers(): List<User>
}
