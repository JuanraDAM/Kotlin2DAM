package com.example.srodenas.example_with_catalogs.ui.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.srodenas.example_with_catalogs.R
import com.example.srodenas.example_with_catalogs.databinding.ActivityMainBinding
import com.example.srodenas.example_with_catalogs.domain.users.models.Profile
import com.example.srodenas.example_with_catalogs.ui.viewmodel.users.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch(Dispatchers.IO) {
            val loggedUser = userViewModel.getUser()
            withContext(Dispatchers.Main) {
                if (loggedUser != null) {
                    Profile.profile.initialize(loggedUser)
                    Log.d("MainActivity", "Profile inicializado con: ${loggedUser.name}")
                } else {
                    Log.e("MainActivity", "No se pudo inicializar el Profile. Redirige al login.")
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                    return@withContext
                }
            }
        }

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.alertsFragment, R.id.usersFragment, R.id.profileFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }
}
