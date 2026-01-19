package com.example.vitalia.network.response

data class LoginResponse(
    val token: String,
    val rol: String // "paciente", "medico", "admin", etc.
)
