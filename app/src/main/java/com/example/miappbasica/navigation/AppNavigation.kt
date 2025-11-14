package com.example.miappbasica.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.miappbasica.ui.screen.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {

            if (currentRoute !in listOf("login", "registro")) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("login") { LoginScreen(navController) }
            composable("registro") { RegistroScreen(navController) }
            composable("inicio") { InicioScreen(navController) }
            composable("bienvenida") { BienvenidaScreen(navController) }
            composable("perfil") { PerfilScreen(navController) }
            composable("configuracion") { ConfiguracionScreen(navController) }
            composable("acerca") { AcercaDeScreen(navController) }


            composable("recetas") { RecetasScreen(navController) }


            composable(
                route = "receta_detalle/{id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id")
                if (id != null) {
                    RecetaDetalleScreen(navController = navController, recetaId = id)
                }
            }
        }
    }
}


