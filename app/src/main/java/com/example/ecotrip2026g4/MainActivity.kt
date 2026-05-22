package com.example.ecotrip2026g4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ecotrip2026g4.data.local.EcoTripDataStore
import com.example.ecotrip2026g4.navigation.FormularioViajeRoute
import com.example.ecotrip2026g4.navigation.ResumenRutaRoute
import com.example.ecotrip2026g4.ui.screen.FormularioScreen
import com.example.ecotrip2026g4.ui.screen.ResumenScreen
import com.example.ecotrip2026g4.ui.theme.EcoTripTheme
import com.example.ecotrip2026g4.ui.viewmodel.EcoTripViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Instancia del gestor de persistencia permanente en disco
        val dataStoreManager = EcoTripDataStore(applicationContext)

        setContent {
            EcoTripTheme {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()

                // Inicialización del ViewModel resiliente
                val ecoTripViewModel: EcoTripViewModel = viewModel()

                // Recolección reactiva de los estados permanentes de DataStore
                val usuarioGlobal by dataStoreManager.nombreUsuarioFlow.collectAsState(initial = "")
                val huellaGlobal by dataStoreManager.bajaHuellaCarbonoFlow.collectAsState(initial = false)

                NavHost(
                    navController = navController,
                    startDestination = FormularioViajeRoute
                ) {
                    // Pantalla 1: Formulario reactivo y persistente
                    composable<FormularioViajeRoute> {
                        FormularioScreen(
                            viewModel = ecoTripViewModel,
                            usuarioGlobal = usuarioGlobal,
                            huellaGlobal = huellaGlobal,
                            onGuardarGlobales = { nombre, huella ->
                                coroutineScope.launch {
                                    dataStoreManager.guardarNombreUsuario(nombre)
                                    dataStoreManager.guardarHuellaCarbono(huella)
                                }
                            },
                            onNavegarAResumen = {
                                // Extraemos de forma segura el valor y lo convertimos con toIntOrNull
                                val diasValidados = ecoTripViewModel.diasDuracion.value.toIntOrNull() ?: 1

                                // Navegación Fuertemente Tipada (Type-Safe Compile Check)
                                navController.navigate(
                                    ResumenRutaRoute(
                                        destino = ecoTripViewModel.destino.value,
                                        diasDuracion = diasValidados,
                                        esViajeGrupal = ecoTripViewModel.esViajeGrupal.value
                                    )
                                )
                            }
                        )
                    }

                    // Pantalla 2: Panel de confirmación adaptativo
                    composable<ResumenRutaRoute> { backStackEntry ->
                        // Deserialización automática del argumento en un objeto tipado por el compilador
                        val argumentosRuta: ResumenRutaRoute = backStackEntry.toRoute()
                        val medioTransporteSeleccionado by ecoTripViewModel.medioTransporte.collectAsState()

                        ResumenScreen(
                            nombreViajero = usuarioGlobal,
                            bajaHuella = huellaGlobal,
                            destino = argumentosRuta.destino,
                            diasDuracion = argumentosRuta.diasDuracion,
                            esViajeGrupal = argumentosRuta.esViajeGrupal,
                            medioTransporte = medioTransporteSeleccionado,
                            onVolverAtras = {
                                /* * Higiene Estricta del Back Stack:
                                 * Retornamos limpiando la pila para evitar redundancias en memoria.
                                 */
                                navController.navigate(FormularioViajeRoute) {
                                    popUpTo(FormularioViajeRoute) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}