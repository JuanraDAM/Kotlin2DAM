package com.example.aplicacionintent

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GetData : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_data)

        // Inicializar los elementos de la UI
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        val btnSaveUserInfo: Button = findViewById(R.id.btnSaveUserInfo)

        // Configurar SharedPreferences
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        // Cargar datos previos si existen
        loadUserInfo()

        // Guardar los datos cuando el usuario presione el botón
        btnSaveUserInfo.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()

            if (name.isNotBlank() && email.isNotBlank()) {
                saveUserInfo(name, email)
                Toast.makeText(this, "Información guardada", Toast.LENGTH_SHORT).show()
                Log.d("GetData", "Información guardada: $name, $email")

                // Verificar que los datos se guardaron
                if (sharedPreferences.getString("USER_NAME", null) == name && sharedPreferences.getString("USER_EMAIL", null) == email) {
                    Log.d("GetData", "Datos verificados, redirigiendo a MainActivity")
                    // Redirigir a MainActivity después de guardar la información
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error al guardar la información", Toast.LENGTH_SHORT).show()
                    Log.e("GetData", "Error al guardar la información")
                }
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                Log.w("GetData", "Campos vacíos")
            }
        }
    }

    private fun saveUserInfo(name: String, email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("USER_NAME", name)
        editor.putString("USER_EMAIL", email)
        editor.apply()
    }

    private fun loadUserInfo() {
        val name = sharedPreferences.getString("USER_NAME", "")
        val email = sharedPreferences.getString("USER_EMAIL", "")
        editTextName.setText(name)
        editTextEmail.setText(email)
        Log.d("GetData", "Datos cargados: $name, $email")
    }
}
