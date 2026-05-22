package com.example.ecotrip2026g4.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecotrip2026g4.data.model.MedioTransporte
import com.example.ecotrip2026g4.ui.viewmodel.EcoTripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    viewModel: EcoTripViewModel,
    usuarioGlobal: String,
    huellaGlobal: Boolean,
    onGuardarGlobales: (String, Boolean) -> Unit,
    onNavegarAResumen: () -> Unit
) {
    // Estados locales para datos globales (DataStore)
    var nombreInput by remember(usuarioGlobal) { mutableStateOf(usuarioGlobal) }
    var huellaInput by remember(huellaGlobal) { mutableStateOf(huellaGlobal) }

    // Estados del ViewModel (persisten en rotación)
    val destino by viewModel.destino.collectAsState()
    val diasDuracion by viewModel.diasDuracion.collectAsState()
    val medioTransporte by viewModel.medioTransporte.collectAsState()
    val esViajeGrupal by viewModel.esViajeGrupal.collectAsState()
    val esFormularioValido by viewModel.esFormularioValido.collectAsState()

    var menuExpandido by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EcoTrip 2026 - Configuración") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            // ✅ El botón se muestra cuando el formulario ES VÁLIDO y hay nombre
            if (esFormularioValido && nombreInput.isNotBlank()) {
                FloatingActionButton(
                    onClick = {
                        // Guardar datos globales en DataStore antes de navegar
                        onGuardarGlobales(nombreInput, huellaInput)
                        onNavegarAResumen()
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Calcular Ruta")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Preferencias Permanentes (DataStore)", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = nombreInput,
                onValueChange = {
                    nombreInput = it
                    // Guardar automáticamente mientras escribe
                    if (it.isNotBlank()) {
                        onGuardarGlobales(it, huellaInput)
                    }
                },
                label = { Text("Nombre del Viajero") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Exclusivamente rutas eco-friendly")
                Switch(
                    checked = huellaInput,
                    onCheckedChange = {
                        huellaInput = it
                        onGuardarGlobales(nombreInput, it)
                    }
                )
            }

            HorizontalDivider()
            Text("Detalles de la Ruta (Resiliente ante Fallos)", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = destino,
                onValueChange = { viewModel.onDestinoChanged(it) },
                label = { Text("Destino del Viaje") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = diasDuracion,
                onValueChange = { viewModel.onDuracionChanged(it) },
                label = { Text("Duración del Viaje (Días)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = diasDuracion.isNotBlank() && diasDuracion.toIntOrNull() == null
            )

            // Selector del Medio de Transporte
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { menuExpandido = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Transporte: ${medioTransporte.nombreDescriptivo}")
                }
                DropdownMenu(
                    expanded = menuExpandido,
                    onDismissRequest = { menuExpandido = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MedioTransporte.entries.forEach { medio ->
                        DropdownMenuItem(
                            text = { Text(medio.nombreDescriptivo) },
                            onClick = {
                                viewModel.onMedioTransporteChanged(medio)
                                menuExpandido = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = esViajeGrupal,
                    onCheckedChange = { viewModel.onViajeGrupalChanged(it) }
                )
                Text("¿Es un viaje grupal / comunitario?")
            }
        }
    }
}