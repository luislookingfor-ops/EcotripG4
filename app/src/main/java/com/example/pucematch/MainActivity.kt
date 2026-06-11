package com.example.pucematch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pucematch.domain.Screen
import com.example.pucematch.ui.theme.PuceMatchTheme

/**
 * Activity principal de PuceMatch.
 * Configura el NavHost con rutas @Serializable fuertemente tipadas.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PuceMatchTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Screen.Login> {
                            // TODO: Fase 2 — Implementar LoginScreen
                            Text("PuceMatch — Login")
                        }

                        composable<Screen.Register> {
                            // TODO: Fase 2 — Implementar RegisterScreen
                            Text("PuceMatch — Registro")
                        }

                        composable<Screen.Home> {
                            // TODO: Fase 2 — Implementar HomeScreen
                            Text("PuceMatch — Home")
                        }

                        composable<Screen.ChatDetail> { backStackEntry ->
                            val chatDetail: Screen.ChatDetail = backStackEntry.toRoute()
                            // TODO: Fase 2 — Implementar ChatDetailScreen
                            Text("PuceMatch — Chat: ${chatDetail.matchId}")
                        }
                    }
                }
            }
        }
    }
}
