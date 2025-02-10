package com.example.proyectoevaluable.ui.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.viewModels
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
import com.example.proyectoevaluable.ui.views.fragments.CardDialogFragment
import com.example.proyectoevaluable.ui.views.fragments.FishingTipsFragment
import com.example.proyectoevaluable.ui.views.fragments.UserFragment
import com.example.proyectoevaluable.ui.views.adapters.MyAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: MyAdapter? = null

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private val viewModel: ListViewModel by viewModels()

    private val items = mutableListOf<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
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
                R.id.nav_profile -> {
                    showUserFragment()
                }
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
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        addButton.setOnClickListener {
            showAddCardDialog()
        }

        // Botones de navegación inferior
        val bottomProfileButton = findViewById<ImageView>(R.id.nav_profile)
        bottomProfileButton.setOnClickListener {
            showUserFragment()
        }

        val navHome = findViewById<ImageView>(R.id.nav_home)
        navHome.setOnClickListener {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            findViewById<FrameLayout>(R.id.fragmentContainer).visibility = View.GONE
            findViewById<View>(R.id.viewOverlayDim).visibility = View.GONE
        }

        val navInfo = findViewById<ImageView>(R.id.nav_info)
        navInfo.setOnClickListener {
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
            }
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
            val newList = items.toMutableList().apply { add(card) }
            viewModel.saveCards(newList)
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
            adapter?.notifyItemChanged(position)
            viewModel.saveCards(items.toMutableList())
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
