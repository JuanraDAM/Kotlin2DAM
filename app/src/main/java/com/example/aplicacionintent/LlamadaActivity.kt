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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class LlamadaActivity : AppCompatActivity() {
    private lateinit var numeroEmergencia: String

    // Registramos el launcher para la solicitud de permisos
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                realizarLlamada()
            } else {
                Toast.makeText(this, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show()
            }
        }

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
            checkPermissionAndCall()
        }

        val btnCambiarNumero: Button = findViewById(R.id.change_phone_number_Button)
        btnCambiarNumero.setOnClickListener {
            val intent = Intent(this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }

        loadUserInfoFooter()
    }

    private fun checkPermissionAndCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Si el permiso no ha sido concedido, lo solicitamos
            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        } else {
            // Si ya tiene el permiso, realizar la llamada
            realizarLlamada()
        }
    }

    private fun realizarLlamada() {
        if (numeroEmergencia.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$numeroEmergencia"))
            startActivity(intent)
        } else {
            Toast.makeText(this, "No se ha configurado un número de emergencia", Toast.LENGTH_SHORT).show()
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
