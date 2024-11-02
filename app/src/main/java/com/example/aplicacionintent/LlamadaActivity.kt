package com.example.aplicacionintent

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class LlamadaActivity : AppCompatActivity() {
    private lateinit var numeroEmergencia: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sos_call)

        // Asegúrate de usar el mismo nombre de SharedPreferences
        val sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE)
        numeroEmergencia = sharedPreferences.getString("numeroEmergencia", null) ?: ""

        val textViewNumero: TextView = findViewById(R.id.textViewNumero)
        textViewNumero.text = "Llamar a: $numeroEmergencia"

        val btnLlamar: ImageView = findViewById(R.id.Sos_call_button)
        btnLlamar.setOnClickListener {
            realizarLlamada()
        }

        val btnCambiarNumero: Button = findViewById(R.id.change_phone_number_Button)
        btnCambiarNumero.setOnClickListener {
            val intent = Intent(this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

        loadUserInfoFooter()
    }

    private fun realizarLlamada() {
        if (numeroEmergencia.isNotEmpty()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
            } else {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$numeroEmergencia"))
                startActivity(intent)
            }
        } else {
            Toast.makeText(this, "No se ha configurado un número de emergencia", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            realizarLlamada()
        }
    }

    private fun loadUserInfoFooter() {
        val sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("USER_NAME", "Nombre no disponible")
        val userEmail = sharedPreferences.getString("USER_EMAIL", "Correo no disponible")

        val userInfoFooter = findViewById<TextView>(R.id.userInfoFooter)
        userInfoFooter.text = "Usuario: $userName | Correo: $userEmail"
    }
}
