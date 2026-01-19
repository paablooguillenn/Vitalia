package com.example.vitalia.network

import com.example.vitalia.network.request.LoginRequest
import com.example.vitalia.network.request.RegisterRequest
import com.example.vitalia.network.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit> // Respuesta vacía, solo nos importa el código 201 (Created)
    
    // Aquí se podrían añadir en el futuro las llamadas para refresh token, etc.
}
