package com.example.vitalia.model

import java.util.Date

data class Appointment(
    val id: String,
    val doctorName: String,
    val date: Date,
    val status: String // Ej: "CONFIRMADA", "CANCELADA", etc.
)