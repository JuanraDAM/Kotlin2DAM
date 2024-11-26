package com.example.proyectoevaluable

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var adapter: MyAdapter? = null
    private var items: MutableList<Card> = mutableListOf()
    private lateinit var currentUser: String
    private lateinit var currentPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // Obtener los datos del usuario y contraseña pasados desde LoginActivity
        currentUser = intent.getStringExtra("USERNAME") ?: "UsuarioDesconocido"
        currentPassword = intent.getStringExtra("PASSWORD") ?: "ContraseñaDesconocida"

        recyclerView = findViewById(R.id.recyclerView)

        // Cargar los datos guardados de SharedPreferences
        items = loadCardsData().toMutableList()

        // Configurar el RecyclerView
        setUpRecyclerView()

        // Configurar el botón de perfil
        val profileButton = findViewById<ImageView>(R.id.nav_profile)
        profileButton.setOnClickListener {
            val loginIntent = Intent(this@ListActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }

        // Configurar el botón flotante para añadir más tarjetas
        val addButton = findViewById<FloatingActionButton>(R.id.button_add)
        addButton.setOnClickListener {
            addCard(currentUser, currentPassword) // Usa los datos del usuario logueado
        }
    }

    private fun setUpRecyclerView() {
        // Configura el LayoutManager y el adaptador
        adapter = MyAdapter(this, items) { position -> deleteCard(position) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun addCard(user: String, password: String) {
        val card = Card(user, password)  // Usar datos del usuario registrado
        items.add(card)
        saveCardsData(items) // Guardar los datos al añadir una tarjeta
        adapter?.notifyItemInserted(items.size - 1) // Notificar al adaptador que se ha añadido una tarjeta
    }

    private fun deleteCard(position: Int) {
        if (position >= 0 && position < items.size) {
            items.removeAt(position)
            saveCardsData(items) // Guardar los datos después de eliminar
            adapter?.notifyItemRemoved(position)
            adapter?.notifyItemRangeChanged(position, items.size)
        }
    }

    private fun saveCardsData(cardsList: List<Card>) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(cardsList) // Convertir lista de Cards a JSON
        editor.putString("cards_data", json)
        editor.apply()
    }

    private fun loadCardsData(): List<Card> {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("cards_data", null)
        val type = object : TypeToken<List<Card>>() {}.type // Cargar lista de Cards desde JSON
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}
