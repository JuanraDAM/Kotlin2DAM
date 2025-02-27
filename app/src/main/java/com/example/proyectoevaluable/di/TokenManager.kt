package com.example.proyectoevaluable.di

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
    private val TOKEN_KEY = "auth_token"
    private val USER_EMAIL_KEY = "user_email"
    private val USER_ID_KEY = "user_id"

    fun saveToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }

    fun clearToken() {
        prefs.edit().remove(TOKEN_KEY).apply()
    }

    fun saveUserEmail(email: String) {
        prefs.edit().putString(USER_EMAIL_KEY, email).apply()
    }

    fun getUserEmail(): String? {
        return prefs.getString(USER_EMAIL_KEY, null)
    }

    fun saveUserId(userId: Int) {
        prefs.edit().putInt(USER_ID_KEY, userId).apply()
    }

    fun getUserId(): Int? {
        val id = prefs.getInt(USER_ID_KEY, -1)
        return if (id == -1) null else id
    }
}
