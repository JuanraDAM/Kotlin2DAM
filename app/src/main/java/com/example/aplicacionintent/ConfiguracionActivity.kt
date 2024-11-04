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

class ConfiguracionActivity : AppCompatActivity() {
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        editTextPhoneNumber = findViewById(R.id.phoneNumberInput) // Asegúrate de que el ID es correcto
        val btnSavePhoneNumber: Button = findViewById(R.id.save_phone_number_Button)

        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        // Guardar el número de teléfono cuando el usuario presione el botón
        btnSavePhoneNumber.setOnClickListener {
            val phoneNumber = editTextPhoneNumber.text.toString()
            if (phoneNumber.isNotBlank()) {
                savePhoneNumber(phoneNumber)
                Toast.makeText(this, "Número de teléfono guardado", Toast.LENGTH_SHORT).show()
                Log.d("ConfiguracionActivity", "Número de teléfono guardado: $phoneNumber")

                // Redirigir a MainActivity después de guardar el número
                startActivity(Intent(this, MainActivity::class.java))
                finish() // Cerrar ConfiguracionActivity para no regresar a ella
            } else {
                Toast.makeText(this, "Por favor, ingrese un número de teléfono", Toast.LENGTH_SHORT).show()
                Log.w("ConfiguracionActivity", "Campo de número de teléfono vacío")
            }
        }
    }

    private fun savePhoneNumber(phoneNumber: String) {
        val editor = sharedPreferences.edit()
        editor.putString("numeroEmergencia", phoneNumber)
        editor.apply()
    }
}