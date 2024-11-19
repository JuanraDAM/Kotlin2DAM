package com.example.aplicacionintent

import android.os.Handler
import android.widget.Toast
import kotlin.random.Random

class LanzadorDeDados(
    private val handler: Handler,
    private val activity: DadosActivity,
    private val objetivo: Int
) : Thread() {

    override fun run() {
        // Generar números aleatorios para los dados
        val dado1 = Random.nextInt(1, 7)
        val dado2 = Random.nextInt(1, 7)

        // Simular un tiempo de espera para el lanzamiento (animación simulada)
        Thread.sleep(200) // 0.2 segundo

        // Actualizar la interfaz gráfica en el hilo principal
        handler.post {
            // Actualizar las imágenes según el resultado
            activity.actualizarImagenesDados(dado1, dado2)

            // Mostrar el resultado
            val suma = dado1 + dado2
            Toast.makeText(activity, "Has sacado $suma", Toast.LENGTH_SHORT).show()

            // Comprobar si el objetivo se alcanzó
            if (suma == objetivo) {
                activity.actualizarBoton(true)
            } else {
                activity.actualizarBoton(false)
            }
        }
    }
}
