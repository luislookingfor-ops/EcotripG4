package com.example.safepass2026

import com.example.safepass2026.model.Asistente

sealed class RegistroState {
    // Este es el estado inicial que definí para cuando la pantalla recién carga
    object Idle : RegistroState()

    // Aquí manejo el caso de éxito; yo paso el objeto Asistente para mostrar los datos
    data class Success(val mensaje: String, val asistente: Asistente) : RegistroState()

    // Este estado lo creé para capturar cualquier error y mostrar un mensaje claro en la UI
    data class Error(val mensaje: String) : RegistroState()
}