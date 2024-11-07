package com.example.proyectoguarreo1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var currentInput: String = "0"
    private var operator: String = ""
    private var previousInput: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)

        // Aquí nos aseguramos que se actualiza la pantalla con la entrada del usuario
        val buttons = arrayOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
            R.id.btn_add, R.id.btn_subtract, R.id.btn_multiply, R.id.btn_divide,
            R.id.btn_decimal, R.id.btn_percentage, R.id.btn_parenthesis
        )

        // Definir la funcionalidad para cada botón
        for (buttonId in buttons) {
            val button: Button = findViewById(buttonId)
            button.setOnClickListener(buttonClickListener)
        }

        // Configurar el botón de igual (calculadora)
        findViewById<Button>(R.id.btn_equals).setOnClickListener {
            calculateResult()
        }

        // Configurar el botón de borrar (AC)
        findViewById<Button>(R.id.btn_ac).setOnClickListener {
            clearDisplay()
        }

        // Configurar el botón de borrar un carácter (⌫)
        findViewById<Button>(R.id.btn_delete).setOnClickListener {
            deleteLastCharacter()
        }
    }

    private val buttonClickListener = View.OnClickListener { v ->
        val button = v as Button
        val text = button.text.toString()

        if (currentInput == "0" && text != "." && text != operator) {
            currentInput = text
        } else {
            currentInput += text
        }

        display.text = currentInput
    }

    private fun calculateResult() {
        try {
            val result = evaluateExpression(currentInput)
            display.text = result.toString()
            currentInput = result.toString()
            previousInput = ""
            operator = ""
        } catch (e: Exception) {
            Toast.makeText(this, "Error en la operación", Toast.LENGTH_SHORT).show()
        }
    }

    private fun evaluateExpression(expression: String): Double {
        // Aquí podemos usar una simple solución para evaluar la expresión.
        // En este caso, por simplicidad, usaremos el evaluador básico:
        return when {
            expression.contains("+") -> {
                val parts = expression.split("+")
                parts[0].toDouble() + parts[1].toDouble()
            }
            expression.contains("-") -> {
                val parts = expression.split("-")
                parts[0].toDouble() - parts[1].toDouble()
            }
            expression.contains("×") -> {
                val parts = expression.split("×")
                parts[0].toDouble() * parts[1].toDouble()
            }
            expression.contains("÷") -> {
                val parts = expression.split("÷")
                parts[0].toDouble() / parts[1].toDouble()
            }
            else -> expression.toDouble()
        }
    }

    private fun clearDisplay() {
        currentInput = "0"
        display.text = currentInput
    }

    private fun deleteLastCharacter() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            if (currentInput.isEmpty()) currentInput = "0"
            display.text = currentInput
        }
    }
}
