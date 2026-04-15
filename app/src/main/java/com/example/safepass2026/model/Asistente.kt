package com.example.safepass2026.model

data class Asistente(
    val nombre: String,
    val edad: Int?, // Blindaje de nulos
    val tipoEntrada: String
)
