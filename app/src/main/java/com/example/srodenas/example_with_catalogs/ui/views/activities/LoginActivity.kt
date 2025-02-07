package com.example.srodenas.example_with_catalogs.ui.views.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.srodenas.example_with_catalogs.R
import com.example.srodenas.example_with_catalogs.databinding.ActivityLoginBinding
import com.example.srodenas.example_with_catalogs.domain.users.models.Profile
import com.example.srodenas.example_with_catalogs.domain.users.models.User
import com.example.srodenas.example_with_catalogs.ui.viewmodel.users.UserViewModel
import com.example.srodenas.example_with_catalogs.ui.views.fragments.users.dialogs.DialogRegisterUser

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    val userViewModel: UserViewModel by viewModels()  // ViewModel del usuario.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa el contexto del ViewModel primero.
        userViewModel.initContext(this)

        // Comprobamos si ya hay un usuario logueado.
        if (isUserLoggedIn()) {
            // Ahora getUser() puede funcionar correctamente porque el contexto ya está inicializado.
            val user = userViewModel.getUser()
            if (user != null) {
                Profile.profile.initialize(user)
            }
            startMainActivity()
            return  // Salimos de onCreate para que no se muestre la pantalla de login.
        }

        // Si no hay usuario logueado, se continúa con la pantalla de login.
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerLiveData()
        initEvent()
    }

    /**
     * Comprueba si las SharedPreferences tienen un usuario guardado.
     */
    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences(
            getString(R.string.pref_user_file),
            Context.MODE_PRIVATE
        )
        val id = sharedPreferences.getInt(getString(R.string.pref_user_id), -1)
        val name = sharedPreferences.getString(getString(R.string.pref_user_name), null)
        val email = sharedPreferences.getString(getString(R.string.pref_user_email), null)
        return (id != -1 && name != null && email != null)
    }

    /**
     * Lanza la MainActivity y limpia la pila de actividades.
     */
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        // Limpiar la pila para evitar volver a la LoginActivity al pulsar Back.
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun initEvent() {
        binding.btnLogin.setOnClickListener {
            userViewModel.isLogin(
                binding.txtEmail.text.toString(),
                binding.txtPassword.text.toString()
            )
        }

        binding.btnRegistro.setOnClickListener {
            val dialog = DialogRegisterUser { user -> okOnRegisterUser(user) }
            dialog.show(this.supportFragmentManager, "Registro de nuevo usuario")
        }
    }

    // Método que registra el usuario a partir del ViewModel.
    private fun okOnRegisterUser(user: User) {
        userViewModel.register(user)
    }

    private fun registerLiveData() {
        userViewModel.isLogginPreferencesLiveData.observe(this, { isLoggin ->
            if (isLoggin) {
                userViewModel.getUser()?.let { user ->
                    Profile.profile.initialize(user)
                    startMainActivity()
                } ?: run {
                    // En caso de que getUser() retorne null (lo cual no debería suceder si isLoggin es true),
                    // se puede mostrar un mensaje de error o forzar un logout.
                    Toast.makeText(this, "Error: usuario no encontrado", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Error en el login", Toast.LENGTH_LONG).show()
            }
        })

        userViewModel.register.observe(this, { register ->
            if (register) {
                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_LONG).show()
            }
        })
    }

}
