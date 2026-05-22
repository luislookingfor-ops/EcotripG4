package com.example.ecotrip2026g4.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecotrip2026g4.data.model.MedioTransporte

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    nombreViajero: String,
    bajaHuella: Boolean,
    destino: String,
    diasDuracion: Int,
    esViajeGrupal: Boolean,
    medioTransporte: MedioTransporte,
    onVolverAtras: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen de Ruta EcoTrip") },
                navigationIcon = {
                    IconButton(onClick = onVolverAtras) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Card(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "¡Ruta Planificada con Éxito!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                // Uso idiomático estricto de interpolación de cadenas de Kotlin
                Text("👤 Viajero Responsable: $nombreViajero", style = MaterialTheme.typography.bodyLarge)
                Text("📍 Destino Ecológico: $destino", style = MaterialTheme.typography.bodyLarge)
                Text("⏱️ Estadía Estimada: $diasDuracion días", style = MaterialTheme.typography.bodyLarge)
                Text("🚍 Tipo de Movilidad: ${medioTransporte.nombreDescriptivo}", style = MaterialTheme.typography.bodyLarge)
                Text("👥 Modalidad de Grupo: ${if (esViajeGrupal) "Sí, Comunitario" else "No, Individual"}", style = MaterialTheme.typography.bodyLarge)

                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (bajaHuella) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = if (bajaHuella) "🌱 Certificación: Ruta de Baja Huella de Carbono Optimizada"
                        else "⚠️ Alerta: Ruta estándar sin restricciones de emisiones ambientales",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (bajaHuella) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}