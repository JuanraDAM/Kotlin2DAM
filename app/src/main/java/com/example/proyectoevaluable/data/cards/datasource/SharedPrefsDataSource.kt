package com.example.proyectoevaluable.data.cards.datasource

import android.content.SharedPreferences
import com.example.proyectoevaluable.domain.cards.models.Card
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class SharedPrefsDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {
    private val currentUserUid: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

    fun saveCards(cards: List<Card>) {
        currentUserUid?.let { uid ->
            val json = gson.toJson(cards)
            sharedPreferences.edit().putString("cards_data_$uid", json).apply()
        }
    }

    fun loadCards(): List<Card> {
        val uid = currentUserUid ?: return emptyList()
        val json = sharedPreferences.getString("cards_data_$uid", null)
        val type = object : TypeToken<List<Card>>() {}.type
        return if (json != null) gson.fromJson(json, type) else emptyList()
    }
}
