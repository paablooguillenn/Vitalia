package com.example.vitalia.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("appointments/files") // Asumimos este endpoint, debe coincidir con tu backend
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("appointmentId") appointmentId: RequestBody
    ): Response<Unit> // Respuesta vacía si todo va bien
}