package com.example.proyectoevaluable.ui.views.activities

import android.content.Intent
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoevaluable.R
import com.example.proyectoevaluable.domain.cards.models.Card
import com.example.proyectoevaluable.ui.viewmodel.cards.ListViewModel
import com.example.proyectoevaluable.ui.views.activities.LoginActivity
import com.example.proyectoevaluable.ui.views.fragments.CardDialogFragment
import com.example.proyectoevaluable.ui.views.fragments.FishingTipsFragment
import com.example.proyectoevaluable.ui.views.fragments.UserFragment
import com.example.proyectoevaluable.ui.views.adapters.MyAdapter
import com.example.proyectoevaluable.di.TokenManager
import com.example.proyectoevaluable.data.auth.AuthRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class ListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: MyAdapter? = null

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private val viewModel: ListViewModel by viewModels()
    private val items = mutableListOf<Card>()

    // Inyectamos AuthRepository y TokenManager
    @Inject lateinit var authRepository: AuthRepository
    @Inject lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        if (tokenManager.getToken().isNullOrEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        val menuButton = findViewById<ImageView>(R.id.button_menu)
        recyclerView = findViewById(R.id.recyclerView)
        val addButton = findViewById<FloatingActionButton>(R.id.button_add)

        setUpRecyclerView()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> showUserFragment()
                R.id.nav_main_list -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
                R.id.nav_second_list -> {
                    val container = findViewById<FrameLayout>(R.id.fragmentContainer)
                    container.visibility = View.VISIBLE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, FishingTipsFragment())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_logout -> {
                    lifecycleScope.launch {
                        val result = authRepository.logout()
                        result.onSuccess {
                            Toast.makeText(this@ListActivity, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@ListActivity, LoginActivity::class.java))
                            finish()
                        }.onFailure { e ->
                            Toast.makeText(this@ListActivity, "Error al cerrar sesión: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        menuButton.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        addButton.setOnClickListener { showAddCardDialog() }

        findViewById<ImageView>(R.id.nav_profile).setOnClickListener { showUserFragment() }
        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            findViewById<FrameLayout>(R.id.fragmentContainer).visibility = View.GONE
            findViewById<View>(R.id.viewOverlayDim).visibility = View.GONE
        }
        findViewById<ImageView>(R.id.nav_info).setOnClickListener {
            val container = findViewById<FrameLayout>(R.id.fragmentContainer)
            container.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FishingTipsFragment())
                .addToBackStack(null)
                .commit()
        }

        viewModel.cards.observe(this, Observer { cards ->
            items.clear()
            items.addAll(cards)
            adapter?.notifyDataSetChanged()
        })

        viewModel.loadCards()
    }

    private fun setUpRecyclerView() {
        adapter = MyAdapter(
            this,
            items,
            onDeleteConfirmed = { position ->
                viewModel.deleteCard(position)
            },
            onEditClicked = { position ->
                showEditCardDialog(position)
            },
            onMapsClicked = { position ->
                openMapForCard(items[position])
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun openMapForCard(card: Card) {
        if (card.image.isNullOrEmpty()) {
            Toast.makeText(this, "No hay imagen para extraer ubicación", Toast.LENGTH_SHORT).show()
            return
        }
        val file = File(Uri.parse(card.image).path ?: "")
        try {
            val exif = ExifInterface(file.absolutePath)
            val latLong = FloatArray(2)
            if (exif.getLatLong(latLong)) {
                openMapWithCoordinates(latLong[0].toDouble(), latLong[1].toDouble())
            } else {
                if (card.latitude != null && card.longitude != null) {
                    openMapWithCoordinates(card.latitude!!, card.longitude!!)
                } else {
                    Toast.makeText(this, "La imagen no contiene datos de ubicación", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al obtener ubicación: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openMapWithCoordinates(latitude: Double, longitude: Double) {
        val geoUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
        val chooser = Intent.createChooser(mapIntent, "Elige una aplicación de mapas")
        if (chooser.resolveActivity(packageManager) != null) {
            startActivity(chooser)
        } else {
            Toast.makeText(this, "No se encontró una aplicación de mapas", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para crear una card usando la API (estructura basada en item)
    private fun showAddCardDialog() {
        val dialog = CardDialogFragment { title, description, weight, base64Image, lat, lon ->
            val card = Card(
                id = null, // El servidor asignará el ID
                title = title,
                description = description,
                weight = weight?.toIntOrNull() ?: 0,
                image = base64Image,
                latitude = lat,
                longitude = lon,
                userId = tokenManager.getUserId() ?: 0
            )
            viewModel.saveCard(card)
        }
        dialog.show(supportFragmentManager, "AddCardDialog")
    }

    // Método para editar una card usando la API
    private fun showEditCardDialog(position: Int) {
        val card = items[position]
        val dialog = CardDialogFragment(
            initialTitle = card.title,
            initialDescription = card.description,
            initialWeight = card.weight.toString(),
            initialPhotoUri = card.image
        ) { title, description, weight, base64Image, lat, lon ->
            val updatedCard = card.copy(
                title = title,
                description = description,
                weight = weight?.toIntOrNull() ?: card.weight,
                image = base64Image,
                latitude = lat,
                longitude = lon
            )
            viewModel.updateCard(updatedCard)
            adapter?.notifyItemChanged(position)
        }
        dialog.show(supportFragmentManager, "EditCardDialog")
    }

    private fun showUserFragment() {
        val overlay = findViewById<View>(R.id.viewOverlayDim)
        overlay.visibility = View.VISIBLE
        val container = findViewById<FrameLayout>(R.id.fragmentContainer)
        container.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, UserFragment())
            .addToBackStack(null)
            .commit()
    }
}
