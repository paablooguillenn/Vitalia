package com.example.vitalia.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Gestiona la sesión del usuario, guardando y recuperando el token de autenticación.
 */
class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("VitaliaAppPrefs", Context.MODE_PRIVATE)

    companion object {
        const val AUTH_TOKEN = "auth_token"
    }

    /**
     * Guarda el token de autenticación en SharedPreferences.
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(AUTH_TOKEN, token)
        editor.apply()
    }

    /**
     * Recupera el token de autenticación de SharedPreferences.
     * @return El token guardado, o null si no existe.
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(AUTH_TOKEN, null)
    }

    /**
     * Borra el token de autenticación (útil para el logout).
     */
    fun clearAuthToken() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}