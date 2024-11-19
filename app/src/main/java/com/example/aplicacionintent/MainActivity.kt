package com.example.aplicacionintent

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var phoneNumber: String? = null  // Declaramos la variable phoneNumber

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        // Verificar si el usuario y correo están guardados
        val userName = sharedPreferences.getString("USER_NAME", null)
        val userEmail = sharedPreferences.getString("USER_EMAIL", null)

        if (userName == null || userEmail == null) {
            startActivity(Intent(this, GetData::class.java))
            finish()
            return
        }

        // Obtener el número de emergencia
        phoneNumber = sharedPreferences.getString("numeroEmergencia", null)

        if (phoneNumber == null) {
            startActivity(Intent(this, ConfiguracionActivity::class.java))
            finish()
            return
        }

        setupButtonActions()  // Configurar los botones
        loadUserInfoFooter()
    }

    private fun setupButtonActions() {
        val callRedirect: ImageView = findViewById(R.id.call_redirect)
        val urlRedirect: ImageView = findViewById(R.id.url_redirect)
        val mailRedirect: ImageView = findViewById(R.id.mail_redirect)
        val alarmRedirect: ImageView = findViewById(R.id.alarm_redirect)
        val btnChistes: ImageView = findViewById(R.id.btnChistes)
        val btnDados: ImageView = findViewById(R.id.btnDados)
        val btnSpinner: ImageView = findViewById(R.id.btnSpinner)

        btnChistes.setOnClickListener {
            startActivity(Intent(this, ChistesActivity::class.java))
        }

        btnDados.setOnClickListener {
            startActivity(Intent(this, DadosActivity::class.java))
        }

        btnSpinner.setOnClickListener {
            startActivity(Intent(this, SpinerActivity::class.java))
        }


        // Configurar el intent para llamar a LlamadaActivity pasando el número de teléfono
        callRedirect.setOnClickListener {
            phoneNumber?.let {
                val llamadaIntent = Intent(this, LlamadaActivity::class.java).apply {
                    putExtra("numeroEmergencia", it)
                }
                startActivity(llamadaIntent)
            }
        }

        // Lógica para abrir una URL personalizada
        urlRedirect.setOnClickListener {
            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"))
            startActivity(urlIntent)
        }

        // Lógica para enviar un correo personalizado
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

        //Lógica para abrir la aplicación de alarmas
        alarmRedirect.setOnClickListener {
            try {
                val intent = Intent(AlarmClock.ACTION_SHOW_ALARMS)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "No se encontró ninguna aplicación de alarma.", Toast.LENGTH_SHORT).show()
            }
        }

        val btnCambiarNumero: Button = findViewById(R.id.btnChangeUserData)
        btnCambiarNumero.setOnClickListener {
            val intent = Intent(this, GetData::class.java)
            startActivity(intent)
        }
    }

    //Metodo que carga las preferencias compartidas
    private fun loadUserInfoFooter() {
        val userName = sharedPreferences.getString("USER_NAME", "Nombre no disponible")
        val userEmail = sharedPreferences.getString("USER_EMAIL", "Correo no disponible")

        val userNameView = findViewById<TextView>(R.id.userName)
        val userEmailView = findViewById<TextView>(R.id.userEmail)

        userNameView.text = "Usuario: $userName"
        userEmailView.text = "Correo: $userEmail"
    }

}
