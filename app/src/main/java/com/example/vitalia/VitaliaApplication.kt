package com.example.vitalia

import android.app.Application
import com.example.vitalia.network.RetrofitClient

class VitaliaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicializamos nuestro cliente de Retrofit pasándole el contexto de la aplicación
        // para que pueda construir el AuthInterceptor.
        RetrofitClient.initialize(this)
    }
}