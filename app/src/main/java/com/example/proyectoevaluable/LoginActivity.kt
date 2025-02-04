package com.example.proyectoevaluable

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
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint  // <-- Añade esta anotación
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Verificar si hay un usuario autenticado
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Si el usuario ya ha iniciado sesión, redirigir a ListActivity
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        // Referencias de los elementos del layout
        val emailEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        // Botón de login
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateEmailPassword(email, password)) {
                loginUser(email, password)
            }
        }

        // Botón de registro
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateEmailPassword(email, password)) {
                registerUser(email, password)
            }
        }

        // Configuración del TextView que se ve como un hipervínculo
        val recoverPasswordText: TextView = findViewById(R.id.recoverPasswordText)
        val content = SpannableString("Recuperar contraseña")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        recoverPasswordText.text = content

        recoverPasswordText.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            recoverPassword(email)
        }

        // Inicialización del botón para mostrar/ocultar la contraseña
        val showPasswordButton = findViewById<ImageButton>(R.id.showPasswordButton)
        showPasswordButton.setOnClickListener {
            if (passwordEditText.inputType == (android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT
                showPasswordButton.setImageResource(R.drawable.ic_eye) // ojo abierto
            } else {
                passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                showPasswordButton.setImageResource(R.drawable.ic_eye) // ojo cerrado
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
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ListActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Verifica tu correo electrónico antes de iniciar sesión", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error al iniciar sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(this, "Registro exitoso. Verifica tu correo electrónico.", Toast.LENGTH_LONG).show()
                    }?.addOnFailureListener {
                        Toast.makeText(this, "Error al enviar el correo de verificación", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error al registrar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun recoverPassword(email: String) {
        if (email.isEmpty()) {
            Toast.makeText(this, "Introduce un correo electrónico", Toast.LENGTH_SHORT).show()
            return
        }
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Toast.makeText(this, "Se ha enviado un correo para recuperar la contraseña", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al enviar el correo: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
