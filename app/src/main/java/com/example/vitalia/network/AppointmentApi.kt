package com.example.vitalia.network

import com.example.vitalia.model.Appointment
import retrofit2.Response
import retrofit2.http.GET

interface AppointmentApi {

    /**
     * Obtiene la lista de citas para el usuario autenticado.
     * El token JWT se añade automáticamente a través del AuthInterceptor.
     */
    @GET("appointments")
    suspend fun getAppointments(): Response<List<Appointment>>
    
    // Aquí podríamos añadir en el futuro GET by ID, POST para crear, PUT para actualizar, etc.
}
