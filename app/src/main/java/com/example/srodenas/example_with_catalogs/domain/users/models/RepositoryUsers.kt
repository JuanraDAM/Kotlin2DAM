package com.example.srodenas.example_with_catalogs.domain.users.models

import com.example.srodenas.example_with_catalogs.data.users.database.dao.UserDao
import com.example.srodenas.example_with_catalogs.data.users.database.entities.UserEntity
import com.example.srodenas.example_with_catalogs.domain.UserDataBaseSingleton

class RepositoryUsers private constructor(private val userDao: UserDao) {
    companion object {
        val repo: RepositoryUsers by lazy {
            RepositoryUsers(UserDataBaseSingleton.userDao)
        }
    }

    // Método para el login (devuelve null si no existe el usuario)
    suspend fun isLoginEntity(email: String, password: String): User? {
        val userEntity: UserEntity? = userDao.login(email, password)
        return userEntity?.let {
            User(it.id, it.name, it.email, it.password, it.phone, it.imag)
        }
    }

    suspend fun registerEntity(user: User): Boolean {
        val existingUser = isLoginEntity(user.email, user.password)  // Usamos user.password
        if (existingUser == null) {
            val userEntity = UserEntity(0, user.name, user.email, user.password, user.phone, user.imag)
            val newId = userDao.insertUser(userEntity)
            user.id = newId.toInt()
            return true
        } else {
            return false
        }
    }

    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers().map { entity ->
            User(entity.id, entity.name, entity.email, entity.password, entity.phone, entity.imag)
        }
    }

    suspend fun deleteUserById(id: Int): Boolean {
        userDao.deleteUserById(id)
        return true
    }

    suspend fun updateUserName(id: Int, nuevoNombre: String): Boolean {
        userDao.updateUserName(id, nuevoNombre)
        return true
    }
}
