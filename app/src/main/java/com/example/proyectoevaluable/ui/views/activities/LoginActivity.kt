package com.example.proyectoevaluable.ui.views.activities

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyectoevaluable.R
import com.example.proyectoevaluable.data.auth.AuthRepository
import com.example.proyectoevaluable.di.TokenManager
import com.example.proyectoevaluable.ui.views.fragments.RecoverPasswordDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Si ya hay token, redirige a ListActivity
        tokenManager.getToken()?.let {
            startActivity(Intent(this, ListActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val recoverPasswordText = findViewById<TextView>(R.id.recoverPasswordText)
        val showPasswordButton = findViewById<ImageButton>(R.id.showPasswordButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (validateEmailPassword(email, password)) {
                loginUser(email, password)
            }
        }

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (validateEmailPassword(email, password)) {
                registerUser(email, password)
            }
        }

        // Configurar el enlace para recuperar contraseña
        val content = SpannableString("Recuperar contraseña")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        recoverPasswordText.text = content
        recoverPasswordText.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Introduce un correo electrónico", Toast.LENGTH_SHORT).show()
            } else {
                // Muestra el diálogo de recuperación sin requerir token
                val dialog = RecoverPasswordDialogFragment.newInstance(email)
                dialog.show(supportFragmentManager, "RecoverPasswordDialogFragment")
            }
        }

        showPasswordButton.setOnClickListener {
            if (passwordEditText.inputType ==
                (android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)
            ) {
                passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT
                showPasswordButton.setImageResource(R.drawable.ic_eye)
            } else {
                passwordEditText.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                showPasswordButton.setImageResource(R.drawable.ic_eye)
            }
            passwordEditText.setSelection(passwordEditText.text.length)
        }
    }

    private fun validateEmailPassword(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, "Introduce un correo electrónico", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty() || password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun loginUser(email: String, password: String) {
        lifecycleScope.launch {
            val result = authRepository.login(email, password)
            result.onSuccess { _ ->
                Toast.makeText(this@LoginActivity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, ListActivity::class.java))
                finish()
            }.onFailure { e ->
                Toast.makeText(this@LoginActivity, "Error al iniciar sesión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        lifecycleScope.launch {
            val result = authRepository.register(email, password)
            result.onSuccess { message ->
                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
            }.onFailure { e ->
                Toast.makeText(this@LoginActivity, "Error al registrar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
