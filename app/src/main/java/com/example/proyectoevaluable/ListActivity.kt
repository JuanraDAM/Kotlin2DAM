package com.example.proyectoevaluable

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
            showAddCardDialog()
        }
    }

    private fun setUpRecyclerView() {
        // Configura el LayoutManager y el adaptador
        adapter = MyAdapter(this, items, { position -> deleteCard(position) }, { position -> showEditCardDialog(position) })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun showAddCardDialog() {
        val dialog = CardDialogFragment { title, description, weight, photoUri ->
            val card = Card(
                username = title,
                password = description,
                weight = weight,
                photoUri = photoUri?.toString()
            )
            items.add(card)
            saveCardsData(items)
            adapter?.notifyItemInserted(items.size - 1)
            recyclerView.scrollToPosition(items.size - 1)
        }
        dialog.show(supportFragmentManager, "AddCardDialog")
    }


    private fun showEditCardDialog(position: Int) {
        val card = items[position]
        val dialog = CardDialogFragment(
            initialTitle = card.username,
            initialDescription = card.password,
            initialWeight = card.weight,
            initialPhotoUri = card.photoUri
        ) { title, description, weight, photoUri ->
            card.username = title
            card.password = description
            card.weight = weight
            card.photoUri = photoUri?.toString()
            saveCardsData(items)
            adapter?.notifyItemChanged(position)
        }
        dialog.show(supportFragmentManager, "EditCardDialog")
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
