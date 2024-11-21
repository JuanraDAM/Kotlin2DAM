package com.example.aplicacionintent

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SpinerActivity : AppCompatActivity() {

    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var spinnerOpciones: Spinner
    private lateinit var btnAceptar: Button
    private lateinit var btnVolverSpiner: Button  // Botón Volver
    private lateinit var checkbox: CheckBox
    private lateinit var switchToggle: Switch
    private lateinit var toggleButton: ToggleButton
    private lateinit var radioGroup: RadioGroup

    // Restante del código...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spiner)

        // Inicialización de los componentes
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        spinnerOpciones = findViewById(R.id.spinnerOpciones)
        btnAceptar = findViewById(R.id.btnAceptar)
        btnVolverSpiner = findViewById(R.id.btnVolverSpiner)  // Inicializar botón Volver
        checkbox = findViewById(R.id.checkbox)
        switchToggle = findViewById(R.id.switchToggle)
        toggleButton = findViewById(R.id.toggleButton)
        radioGroup = findViewById(R.id.radioGroup)

        // Configurar el botón Volver
        btnVolverSpiner.setOnClickListener {
            // Navegar a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Finaliza la actividad actual
        }

        // Configuración del AutoCompleteTextView con una lista de nombres
        val nombres = arrayOf("Juan", "Ana", "Carlos", "Pedro", "Maria", "Lucía", "Laura")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombres)
        autoCompleteTextView.setAdapter(adapter)

        // Configurar Spinner con opciones
        ArrayAdapter.createFromResource(
            this,
            R.array.componentes_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerOpciones.adapter = adapter
        }

        // Evento del botón Aceptar
        btnAceptar.setOnClickListener {
            val selectedOption = spinnerOpciones.selectedItem.toString()
            handleSelection(selectedOption)
        }

        // Manejo del estado del CheckBox
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            val checkText = if (isChecked) "Checkbox está marcado" else "Checkbox no está marcado"
            Toast.makeText(this, checkText, Toast.LENGTH_SHORT).show()
        }

        // Manejo del estado del Switch
        switchToggle.setOnCheckedChangeListener { _, isChecked ->
            val switchText = if (isChecked) "Switch está activado" else "Switch está desactivado"
            Toast.makeText(this, switchText, Toast.LENGTH_SHORT).show()
        }

        // Manejo del ToggleButton
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            val toggleText = if (isChecked) "Toggle ON" else "Toggle OFF"
            Toast.makeText(this, toggleText, Toast.LENGTH_SHORT).show()
        }

        // Manejo de la selección en RadioGroup
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            Toast.makeText(this, "Seleccionaste: ${selectedRadioButton.text}", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para manejar las selecciones del Spinner
    private fun handleSelection(option: String) {
        when (option) {
            "Button" -> Toast.makeText(this, "Botón seleccionado", Toast.LENGTH_SHORT).show()
            "TextView" -> Toast.makeText(this, "Texto actual: ${autoCompleteTextView.text}", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, "Selecciona una opción válida", Toast.LENGTH_SHORT).show()
        }
    }
}
