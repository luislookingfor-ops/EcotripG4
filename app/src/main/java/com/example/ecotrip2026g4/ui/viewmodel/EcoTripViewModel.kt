package com.example.ecotrip2026g4.ui.viewmodel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecotrip2026g4.data.model.MedioTransporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EcoTripViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Claves de estado para SavedStateHandle (Blindaje contra Process Death)
    companion object {
        private const val KEY_DESTINO = "form_destino"
        private const val KEY_DURACION = "form_duracion"
    }

    // 1. Estados manejados por SavedStateHandle (UI State Reactivo)
    val destino = savedStateHandle.getStateFlow(KEY_DESTINO, "")
    val diasDuracion = savedStateHandle.getStateFlow(KEY_DURACION, "")

    // 2. Estados temporales en memoria (ViewModel ordinario)
    private val _medioTransporte = MutableStateFlow(MedioTransporte.TREN)
    val medioTransporte: StateFlow<MedioTransporte> = _medioTransporte.asStateFlow()

    private val _esViajeGrupal = MutableStateFlow(false)
    val esViajeGrupal: StateFlow<Boolean> = _esViajeGrupal.asStateFlow()

    // Funciones modificadoras de Estado (Eventos de la UI)
    fun onDestinoChanged(nuevoDestino: String) {
        savedStateHandle[KEY_DESTINO] = nuevoDestino
    }

    fun onDuracionChanged(nuevaDuracion: String) {
        // Mindset Kotlin Moderno: Desinfección de entrada numérica en tiempo real
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

    // Validación segura antes de permitir la navegación
    fun esFormularioValido(): Boolean {
        val dias = diasDuracion.value.toIntOrNull()
        return destino.value.isNotBlank() && dias != null && dias > 0
    }
}