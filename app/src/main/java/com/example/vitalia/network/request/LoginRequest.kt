package com.example.vitalia.network.request

data class LoginRequest(
    val email: String,
    val contrasena: String // Asegúrate de que el nombre del campo coincida con lo que espera tu backend
)
