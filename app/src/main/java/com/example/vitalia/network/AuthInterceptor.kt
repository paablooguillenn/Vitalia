package com.example.vitalia.network

import android.content.Context
import com.example.vitalia.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor de OkHttp que añade el token de autenticación a las cabeceras de las peticiones.
 */
class AuthInterceptor(context: Context) : Interceptor {

    // Usamos una instancia de SessionManager para acceder al token guardado.
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // Recuperamos el token
        sessionManager.fetchAuthToken()?.let {
            // Si existe, lo añadimos a la cabecera 'Authorization'
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}