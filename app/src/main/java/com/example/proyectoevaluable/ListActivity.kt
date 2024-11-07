package com.example.proyectoevaluable

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


class ListActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var user: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)


        // Obtener usuario y contraseña del intent
        val intent = intent
        user = intent.getStringExtra("USERNAME")
        password = intent.getStringExtra("PASSWORD")

        // Configurar RecyclerView y pasar usuario y contraseña a cada elemento
        setUpRecyclerView()

        // Configurar el botón de perfil para volver a la pantalla de login
        val profileButton = findViewById<ImageView>(R.id.nav_profile)
        profileButton.setOnClickListener { // Crear intent explícito para volver al LoginActivity
            val loginIntent = Intent(
                this@ListActivity,
                LoginActivity::class.java
            )
            startActivity(loginIntent)
            finish()
        }
    }

    private fun setUpRecyclerView() {
        // Crear el adaptador para el RecyclerView, pasando usuario y contraseña
        val adapter: MyAdapter = MyAdapter(user, password)
        recyclerView!!.adapter = adapter
    }
}