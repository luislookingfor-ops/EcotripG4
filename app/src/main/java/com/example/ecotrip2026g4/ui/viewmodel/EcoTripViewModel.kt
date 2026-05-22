package com.example.ecotrip2026g4.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecotrip2026g4.data.model.MedioTransporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch  // ✅ IMPORTANTE: Agregar este import

class EcoTripViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_DESTINO = "form_destino"
        private const val KEY_DURACION = "form_duracion"
    }

    // Estados con SavedStateHandle (persisten al rotar pantalla)
    val destino = savedStateHandle.getStateFlow(KEY_DESTINO, "")
    val diasDuracion = savedStateHandle.getStateFlow(KEY_DURACION, "")

    // Estados temporales en memoria
    private val _medioTransporte = MutableStateFlow(MedioTransporte.TREN)
    val medioTransporte: StateFlow<MedioTransporte> = _medioTransporte.asStateFlow()

    private val _esViajeGrupal = MutableStateFlow(false)
    val esViajeGrupal: StateFlow<Boolean> = _esViajeGrupal.asStateFlow()

    // Estado reactivo para validación del formulario
    private val _esFormularioValido = MutableStateFlow(false)
    val esFormularioValido: StateFlow<Boolean> = _esFormularioValido.asStateFlow()

    // Inicializar validación reactiva ✅ CORREGIDO
    init {
        viewModelScope.launch {  // ✅ Usar viewModelScope, no MainScope
            destino.collect { _ ->
                _esFormularioValido.value = esFormularioValido()
            }
        }
        viewModelScope.launch {  // ✅ Usar viewModelScope, no MainScope
            diasDuracion.collect { _ ->
                _esFormularioValido.value = esFormularioValido()
            }
        }
    }

    // Funciones modificadoras de Estado (Eventos de la UI)
    fun onDestinoChanged(nuevoDestino: String) {
        savedStateHandle[KEY_DESTINO] = nuevoDestino
    }

    fun onDuracionChanged(nuevaDuracion: String) {
        // Validación: solo números o vacío
        if (nuevaDuracion.isEmpty() || nuevaDuracion.toIntOrNull() != null) {
            savedStateHandle[KEY_DURACION] = nuevaDuracion
        }
    }

    fun onMedioTransporteChanged(medio: MedioTransporte) {
        _medioTransporte.value = medio
    }

    fun onViajeGrupalChanged(esGrupal: Boolean) {
        _esViajeGrupal.value = esGrupal
    }

    // Validación del formulario
    fun esFormularioValido(): Boolean {
        val dias = diasDuracion.value.toIntOrNull()
        return destino.value.isNotBlank() && dias != null && dias > 0
    }
}
