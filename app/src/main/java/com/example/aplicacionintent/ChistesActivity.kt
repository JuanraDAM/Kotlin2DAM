package com.example.aplicacionintent

import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ChistesActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var txtChiste: TextView
    private lateinit var btnContarChiste: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var textToSpeech: TextToSpeech

    private val chistes = arrayOf(
        "¿Por qué los pájaros no usan Facebook? Porque ya tienen Twitter.",
        "¿Qué le dice un gusano a otro gusano? Voy a dar una vuelta a la manzana.",
        "¿Por qué las focas miran siempre hacia arriba? ¡Porque ahí están los focos!",
        "¿Cómo maldice un pollito a otro pollito? ¡Cal...dito seas!",
        "¿Sabes cómo se llama el campeón de buceo japonés? Tokofondo. ¿Y el subcampeón? Kasitoko."
    )

    private var isFirstClick = true  // Variable para verificar si es la primera vez que se hace clic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chistes)

        // Inicialización de los componentes
        txtChiste = findViewById(R.id.txtChiste)
        btnContarChiste = findViewById(R.id.btnContarChiste)
        progressBar = findViewById(R.id.progressBar)

        // Inicializar TextToSpeech
        textToSpeech = TextToSpeech(this, this)

        // Evento al pulsar el botón
        btnContarChiste.setOnClickListener {
            if (isFirstClick) {
                // En la primera vez, solo decimos "Contar chiste"
                textToSpeech.speak("Contar chiste", TextToSpeech.QUEUE_FLUSH, null, null)
                isFirstClick = false  // Cambiamos el estado a no ser el primer clic
            } else {
                // Mostrar el ProgressBar (carga giratoria)
                progressBar.visibility = android.view.View.VISIBLE

                // Hacer un retraso de 2 segundos para simular la carga
                Handler().postDelayed({
                    // Seleccionar un chiste aleatorio
                    val chisteAleatorio = chistes.random()

                    // Mostrar el chiste en el TextView
                    txtChiste.text = chisteAleatorio

                    // Leer el chiste en voz alta
                    textToSpeech.speak(chisteAleatorio, TextToSpeech.QUEUE_FLUSH, null, null)

                    // Ocultar el ProgressBar después de mostrar el chiste
                    progressBar.visibility = android.view.View.GONE
                }, 2000) // 2 segundos de retraso
            }
        }
    }

    // Inicialización de TextToSpeech
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Puedes configurar el idioma aquí si lo deseas, por ejemplo:
            val langResult = textToSpeech.setLanguage(Locale("es", "ES"))
            if (langResult == TextToSpeech.LANG_MISSING_DATA ||
                langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Idioma no soportado o faltan datos", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Error al inicializar TextToSpeech", Toast.LENGTH_SHORT).show()
        }
    }

    // Liberar recursos de TextToSpeech
    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}
