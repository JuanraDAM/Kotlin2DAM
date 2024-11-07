package com.example.proyectoevaluable

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


class LoginActivity : AppCompatActivity() {
    // Definir constantes
    private val MYUSER: String = "usuario"
    private val MYPASS: String = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Obtener referencias de los campos y el botón de login
        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        // Configurar el listener del botón de login
        loginButton.setOnClickListener {
            val enteredUser = usernameEditText.text.toString()
            val enteredPass = passwordEditText.text.toString()

            // Verificar usuario y contraseña
            if (MYUSER == enteredUser && MYPASS == enteredPass) {
                // Usuario y contraseña son correctos
                val intent = Intent(
                    this@LoginActivity,
                    ListActivity::class.java
                )
                // Pasar datos a la siguiente actividad
                intent.putExtra("USERNAME", enteredUser)
                intent.putExtra("PASSWORD", enteredPass)
                startActivity(intent)
                finish()
            } else {
                // Usuario o contraseña incorrectos
                Toast.makeText(
                    this@LoginActivity,
                    "Usuario o contraseña incorrectos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}