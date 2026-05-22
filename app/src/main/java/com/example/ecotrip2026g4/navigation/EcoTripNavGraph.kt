package com.example.ecotrip2026g4.navigation

import kotlinx.serialization.Serializable

// Pantalla 1: Formulario ultra-resiliente. No requiere argumentos.
@Serializable
object FormularioViajeRoute

/*
 * Pantalla 2: Panel dinámico de confirmación de ruta.
 * Exige parámetros estrictos inyectados de forma segura a través del compilador.
 */
@Serializable
data class ResumenRutaRoute(
    val destino: String,
    val diasDuracion: Int,
    val esViajeGrupal: Boolean
)