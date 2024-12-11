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
import com.google.firebase.auth.FirebaseAuth

class ListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var adapter: MyAdapter? = null
    private var items: MutableList<Card> = mutableListOf()
    private lateinit var currentUserUid: String // UID del usuario actual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // Obtener UID del usuario actual autenticado
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            currentUserUid = firebaseUser.uid
        } else {
            // Si no hay un usuario autenticado, redirigir a la pantalla de inicio de sesión
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
            return
        }

        recyclerView = findViewById(R.id.recyclerView)

        // Cargar los datos guardados para el usuario actual
        items = loadCardsData().toMutableList()

        // Configurar RecyclerView
        setUpRecyclerView()

        // Botón de perfil
        val profileButton = findViewById<ImageView>(R.id.nav_profile)
        profileButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Cerrar sesión
            val loginIntent = Intent(this@ListActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }

        // Botón flotante para añadir tarjetas
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
        // Guardar datos usando una clave única por usuario (UID)
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(cardsList)
        editor.putString("cards_data_$currentUserUid", json) // Clave única con UID
        editor.apply()
    }

    private fun loadCardsData(): List<Card> {
        // Cargar datos específicos del usuario actual
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("cards_data_$currentUserUid", null) // Clave única con UID
        val type = object : TypeToken<List<Card>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}