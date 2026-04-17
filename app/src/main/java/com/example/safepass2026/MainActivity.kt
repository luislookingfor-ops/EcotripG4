package com.example.safepass2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.safepass2026.model.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SafePassApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafePassApp() {
    // Estados de los campos
    var nombreText by remember { mutableStateOf("") }
    var edadText by remember { mutableStateOf("") }

    // Estado de la UI (Sealed Class de Yuli)
    var uiState by remember { mutableStateOf<RegistroState>(RegistroState.Idle) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { CenterAlignedTopAppBar(title = { Text("SafePass 2026 - Grupo 4") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (val state = uiState) {
                is RegistroState.Idle -> {
                    FormularioRegistro(
                        nombre = nombreText,
                        onNombreChange = { nombreText = it },
                        edad = edadText,
                        onEdadChange = { edadText = it },
                        onRegistrar = {
                            // Evitamos crash si edadText está vacío
                            val edadNumerica = edadText.toIntOrNull()

                            // Uso de let para validar el nombre
                            nombreText.takeIf { it.isNotBlank() }?.let { nombreLimpio ->
                                // Uso de la Extension Function de Kevin
                                if (edadNumerica.esMayorDeEdad()) {

                                    // Uso de apply para configurar el objeto (Scope Function)
                                    val asistente = Asistente(nombreLimpio, edadNumerica, "General").apply {
                                        println("Objeto configurado para: $nombre")
                                    }

                                    uiState = RegistroState.Success("Registro completado con éxito", asistente)
                                } else {
                                    uiState = RegistroState.Error("Acceso denegado: El asistente debe ser mayor de edad.")
                                }
                            } ?: run {
                                uiState = RegistroState.Error("Error: El nombre es obligatorio.")
                            }
                        }
                    )
                }
                is RegistroState.Success -> {
                    // Estado de éxito mensaje en verde
                    Text(
                        text = "${state.mensaje}\n\nBienvenido: ${state.asistente.nombre}",
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Button(onClick = { uiState = RegistroState.Idle }) { Text("Volver") }
                }
                is RegistroState.Error -> {
                    // Estado de error mensaje en rojo
                    Text(text = state.mensaje, color = Color.Red, style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = { uiState = RegistroState.Idle }) { Text("Reintentar") }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun FormularioRegistro(
    nombre: String, onNombreChange: (String) -> Unit,
    edad: String, onEdadChange: (String) -> Unit,
    onRegistrar: () -> Unit
) {
    OutlinedTextField(value = nombre, onValueChange = onNombreChange, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(value = edad, onValueChange = onEdadChange, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth())
    Button(onClick = onRegistrar, modifier = Modifier.fillMaxWidth()) {
        Text("Registrar")
    }
}