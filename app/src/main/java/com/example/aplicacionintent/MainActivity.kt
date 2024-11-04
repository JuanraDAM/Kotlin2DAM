package com.example.aplicacionintent

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar SharedPreferences
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        // Comprobar si el nombre y correo están guardados
        val userName = sharedPreferences.getString("USER_NAME", null)
        val userEmail = sharedPreferences.getString("USER_EMAIL", null)

        // Si no hay usuario ni correo, redirigir a GetData
        if (userName == null || userEmail == null) {
            startActivity(Intent(this, GetData::class.java))
            finish() // Termina MainActivity para no regresar a ella
            return // Salir del método onCreate para evitar ejecutar el resto del código
        }

        // Comprobar si hay un número de teléfono guardado
        val phoneNumber = sharedPreferences.getString("numeroEmergencia", null)

        // Si no hay número de teléfono, redirigir a ConfiguracionActivity
        if (phoneNumber == null) {
            startActivity(Intent(this, ConfiguracionActivity::class.java))
            finish() // Termina MainActivity para no regresar a ella
            return // Salir del método onCreate para evitar ejecutar el resto del código
        }

        // Aquí va el resto de la lógica de MainActivity, como los clics de los botones
        setupButtonActions()
        loadUserInfoFooter()

        // Manejar el clic del botón para cambiar datos de usuario
        val btnChangeUserData: Button = findViewById(R.id.btnChangeUserData)
        btnChangeUserData.setOnClickListener {
            val intent = Intent(this, GetData::class.java)
            startActivity(intent)
            finish() // Opcional: puedes terminar MainActivity si no quieres regresar a ella
        }
    }

    private fun setupButtonActions() {
        val callRedirect: ImageView = findViewById(R.id.call_redirect)
        val urlRedirect: ImageView = findViewById(R.id.url_redirect)
        val mailRedirect: ImageView = findViewById(R.id.mail_redirect)
        val alarmRedirect: ImageView = findViewById(R.id.alarm_redirect)

        // Lógica para redirigir a la actividad de llamada
        callRedirect.setOnClickListener {
            val llamadaIntent = Intent(this, LlamadaActivity::class.java)
            startActivity(llamadaIntent)
        }

        // Lógica para abrir una URL
        urlRedirect.setOnClickListener {
            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"))
            startActivity(urlIntent)
        }

        // Lógica para enviar un correo
        mailRedirect.setOnClickListener {
            val userEmail = sharedPreferences.getString("USER_EMAIL", null)

            if (userEmail != null) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$userEmail")
                    putExtra(Intent.EXTRA_SUBJECT, "Asunto del correo")
                    putExtra(Intent.EXTRA_TEXT, "Contenido del mensaje")
                }

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://mail.google.com/"))
                    startActivity(webIntent)
                }
            } else {
                Toast.makeText(this, "No se ha encontrado un correo guardado", Toast.LENGTH_SHORT).show()
            }
        }

        // Lógica para el botón de alarma
        alarmRedirect.setOnClickListener {
            try {
                val intent = Intent(AlarmClock.ACTION_SHOW_ALARMS)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "No se encontró ninguna aplicación de alarma.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUserInfoFooter() {
        val userName = sharedPreferences.getString("USER_NAME", "Nombre no disponible")
        val userEmail = sharedPreferences.getString("USER_EMAIL", "Correo no disponible")

        val userInfoFooter = findViewById<TextView>(R.id.userInfoFooter)
        userInfoFooter.text = "Usuario: $userName | Correo: $userEmail"
    }
}
