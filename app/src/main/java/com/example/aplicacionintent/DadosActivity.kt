package com.example.aplicacionintent

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class DadosActivity : AppCompatActivity() {

    private lateinit var imgDado1: ImageView
    private lateinit var imgDado2: ImageView
    private lateinit var btnLanzar: Button
    private lateinit var btnRegresar: Button
    private var objetivo: Int = 0
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dados)

        // Inicializamos las vistas
        imgDado1 = findViewById(R.id.imgDado1)
        imgDado2 = findViewById(R.id.imgDado2)
        btnLanzar = findViewById(R.id.btnLanzar)
        btnRegresar = findViewById(R.id.btnRegresar)
        btnRegresar.setBackgroundColor(Color.GRAY)

        // Inicializamos el handler después de que el contexto está completamente disponible
        handler = Handler(Looper.getMainLooper())

        // Determinamos un objetivo aleatorio entre 2 y 12 (suma posible de dos dados)
        objetivo = Random.nextInt(2, 13)
        Log.d("DadosActivity", "Objetivo determinado: $objetivo")  // Agregamos log para verificar

        Toast.makeText(this, "Tu objetivo es $objetivo", Toast.LENGTH_LONG).show()

        // Configuramos el evento de clic en el botón "Lanzar"
        btnLanzar.setOnClickListener {
            Log.d("DadosActivity", "Botón presionado para lanzar dados") // Agregamos log
            // Deshabilitar el botón mientras se realiza el lanzamiento
            btnLanzar.isEnabled = false

            // Crear y ejecutar el hilo para lanzar los dados
            LanzadorDeDados(handler, this, objetivo).start()
        }

        // Inicialmente deshabilitamos el botón "Volver al Menú"
        btnRegresar.isEnabled = false

        // Configuramos el evento de clic en el botón "Regresar"
        btnRegresar.setOnClickListener {
            // Volver al MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Terminamos la actividad actual
        }
    }

    // Método público para acceder a las imágenes de los dados
    fun actualizarImagenesDados(dado1: Int, dado2: Int) {
        Log.d("DadosActivity", "Actualizando imágenes de los dados")  // Log cuando se actualizan las imágenes
        imgDado1.setImageResource(getDadoDrawable(dado1))
        imgDado2.setImageResource(getDadoDrawable(dado2))
    }

    // Método público para actualizar el estado del botón
    fun actualizarBoton(alcanzado: Boolean) {
        Log.d("DadosActivity", "Actualizando estado del botón")  // Log cuando se actualiza el botón
        if (alcanzado) {
            // Objetivo alcanzado, habilitar botón para volver al menú
            btnLanzar.setBackgroundColor(Color.GRAY)
            btnLanzar.setTextColor(Color.WHITE) // Opcional: cambiar el color del texto
            btnLanzar.isEnabled = false
            btnRegresar.isEnabled = true // Habilitar el botón de regreso
            btnRegresar.setBackgroundColor(Color.BLUE)
            Toast.makeText(this, "¡Objetivo alcanzado! El botón se ha habilitado para regresar al menú.", Toast.LENGTH_LONG).show()
        } else {
            btnLanzar.isEnabled = true
            btnRegresar.isEnabled = false // Deshabilitar el botón de regreso si el objetivo no es alcanzado
        }
    }

    private fun getDadoDrawable(numero: Int): Int {
        return when (numero) {
            1 -> R.drawable.dado1
            2 -> R.drawable.dado2
            3 -> R.drawable.dado3
            4 -> R.drawable.dado4
            5 -> R.drawable.dado5
            6 -> R.drawable.dado6
            else -> R.drawable.dado1
        }
    }

    override fun onBackPressed() {
        if (btnLanzar.isEnabled) {
            Toast.makeText(this, "No puedes volver atrás hasta alcanzar el objetivo.", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
    }
}
