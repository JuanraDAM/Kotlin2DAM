package com.example.srodenas.example_with_catalogs.repository

import com.example.srodenas.example_with_catalogs.domain.users.models.RepositoryUsers
import com.example.srodenas.example_with_catalogs.domain.users.models.User

class UsuarioRepository : IUserRepository {
    private val repositoryUsers = RepositoryUsers.repo

    override suspend fun getAllUsers(): List<User> {
        return repositoryUsers.getAllUsers()
    }

    override suspend fun registrarUsuario(usuario: User): Boolean {
        return repositoryUsers.registerEntity(usuario)
    }

    override suspend fun login(email: String, passw: String): User? {
        return repositoryUsers.isLoginEntity(email, passw)
    }

    override suspend fun eliminarUsuarioPorId(id: Int): Boolean {
        return repositoryUsers.deleteUserById(id)
    }

    override suspend fun actualizarNombre(id: Int, nuevoNombre: String): Boolean {
        return repositoryUsers.updateUserName(id, nuevoNombre)
    }
}

