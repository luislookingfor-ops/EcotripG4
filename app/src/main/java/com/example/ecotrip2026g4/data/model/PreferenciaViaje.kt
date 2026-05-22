package com.example.ecotrip2026g4.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PreferenciaViaje(
    val nombreViajero: String,
    val medioTransporte: MedioTransporte,
    val bajaHuellaCarbono: Boolean
)