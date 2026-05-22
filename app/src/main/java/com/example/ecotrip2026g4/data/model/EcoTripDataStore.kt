package com.example.ecotrip2026g4.data.model

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión global para instanciar DataStore como Singleton único
val Context.dataStore by preferencesDataStore(name = "ecotrip_preferences")

class EcoTripDataStore(private val context: Context) {

    companion object {
        val KEY_NOMBRE_USUARIO = stringPreferencesKey("nombre_usuario")
        val KEY_HUELLA_CARBONO = booleanPreferencesKey("baja_huella_carbono")
    }

    // Flujo de lectura asíncrona de las preferencias
    val nombreUsuarioFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_NOMBRE_USUARIO] ?: ""
    }

    val bajaHuellaCarbonoFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_HUELLA_CARBONO] ?: false
    }

    // Funciones de escritura asíncrona
    suspend fun guardarNombreUsuario(nombre: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NOMBRE_USUARIO] = nombre
        }
    }

    suspend fun guardarHuellaCarbono(activado: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_HUELLA_CARBONO] = activado
        }
    }
}