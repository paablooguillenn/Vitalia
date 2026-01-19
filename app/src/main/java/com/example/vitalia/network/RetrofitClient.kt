package com.example.vitalia.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"

    // Dejamos el cliente OkHttp y Retrofit como lateinit para inicializarlos después
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit

    // Las instancias de los servicios también serán lazy-initialized
    val instance: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val appointmentApi: AppointmentApi by lazy {
        retrofit.create(AppointmentApi::class.java)
    }

    /**
     * Inicializa el cliente Retrofit con el contexto de la aplicación.
     * Este método debe ser llamado desde la clase Application.
     */
    fun initialize(context: Context) {
        // El interceptor de logging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // El interceptor de autenticación que creamos
        val authInterceptor = AuthInterceptor(context)

        // Construimos el OkHttpClient añadiendo AMBOS interceptores
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        // Construimos Retrofit usando el OkHttpClient configurado
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}