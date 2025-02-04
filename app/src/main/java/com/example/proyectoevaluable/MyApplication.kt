package com.example.proyectoevaluable

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Aquí puedes inicializar configuraciones globales si lo necesitas.
    }
}
