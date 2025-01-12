package com.example.proyectoevaluable

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: MyAdapter? = null
    private var items: MutableList<Card> = mutableListOf()
    private lateinit var currentUserUid: String

    // DrawerLayout y NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

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

        // Referencias a vistas
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        val menuButton = findViewById<ImageView>(R.id.button_menu)
        recyclerView = findViewById(R.id.recyclerView)
        val addButton = findViewById<FloatingActionButton>(R.id.button_add)

        // Configurar RecyclerView con adaptador
        items = loadCardsData().toMutableList()
        setUpRecyclerView()

        // Configurar eventos del NavigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_crud -> {
                    // 1) Muestra la capa semitransparente
                    val overlay = findViewById<View>(R.id.viewOverlayDim)
                    overlay.visibility = View.VISIBLE

                    // 2) Haz visible el contenedor del fragment
                    val container = findViewById<FrameLayout>(R.id.fragmentContainer)
                    container.visibility = View.VISIBLE

                    // 3) Reemplaza con tu UserFragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, UserFragment())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_lista -> {
                    // Cierra cualquier fragment
                    supportFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    // Oculta ambos
                    findViewById<FrameLayout>(R.id.fragmentContainer).visibility = View.GONE
                    findViewById<View>(R.id.viewOverlayDim).visibility = View.GONE
                }
                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START) // Cierra el menú
            true
        }

        // Al hacer click en el botón hamburguesa, abrimos el Drawer
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Botón flotante para añadir tarjetas
        addButton.setOnClickListener {
            showAddCardDialog()
        }

        // Botón de perfil en el bottom nav (lo usabas para logout)
        val profileButton = findViewById<ImageView>(R.id.nav_profile)
        profileButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val loginIntent = Intent(this@ListActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    private fun setUpRecyclerView() {
        adapter = MyAdapter(
            this,
            items,
            { position -> deleteCard(position) },
            { position -> showEditCardDialog(position) }
        )
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
            saveCardsData(items)
            adapter?.notifyItemRemoved(position)
            adapter?.notifyItemRangeChanged(position, items.size)
        }
    }

    private fun saveCardsData(cardsList: List<Card>) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(cardsList)
        editor.putString("cards_data_$currentUserUid", json)
        editor.apply()
    }

    private fun loadCardsData(): List<Card> {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("cards_data_$currentUserUid", null)
        val type = object : TypeToken<List<Card>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}
