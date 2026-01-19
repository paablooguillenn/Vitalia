package com.example.vitalia.network.request

/**
 * Define los datos necesarios para registrar un nuevo usuario.
 * Los nombres de las variables deben coincidir con lo que espera la API del backend.
 */
data class RegisterRequest(
    val nombre: String, // O el nombre que espere tu API, como "name" o "fullName"
    val email: String,
    val contrasena: String // O "password", etc.
)
