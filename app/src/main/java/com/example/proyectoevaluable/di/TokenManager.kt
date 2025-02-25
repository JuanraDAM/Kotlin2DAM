package com.example.proyectoevaluable.di

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val TOKEN_KEY = "TOKEN_KEY"
        private const val USER_EMAIL_KEY = "current_user_email"
        private const val USER_ID_KEY = "current_user_id"
    }

    fun saveToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? = prefs.getString(TOKEN_KEY, null)

    fun clearToken() {
        prefs.edit().remove(TOKEN_KEY).apply()
        prefs.edit().remove(USER_EMAIL_KEY).apply()
        prefs.edit().remove(USER_ID_KEY).apply()
    }

    // Métodos para el email del usuario
    fun saveUserEmail(email: String) {
        prefs.edit().putString(USER_EMAIL_KEY, email).apply()
    }

    fun getUserEmail(): String? = prefs.getString(USER_EMAIL_KEY, null)

    // Métodos para el ID del usuario
    fun saveUserId(id: Int) {
        prefs.edit().putInt(USER_ID_KEY, id).apply()
    }

    fun getUserId(): Int? {
        val id = prefs.getInt(USER_ID_KEY, -1)
        return if (id == -1) null else id
    }
}
