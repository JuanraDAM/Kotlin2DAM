package com.example.proyectoevaluable

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {

    private val currentUserUid: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

    fun saveCardsData(cardsList: List<Card>) {
        currentUserUid?.let { uid ->
            val editor = sharedPreferences.edit()
            val json = gson.toJson(cardsList)
            editor.putString("cards_data_$uid", json)
            editor.apply()
        }
    }

    fun loadCardsData(): List<Card> {
        val uid = currentUserUid ?: return emptyList()
        val json = sharedPreferences.getString("cards_data_$uid", null)
        val type = object : TypeToken<List<Card>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}

