package com.example.miappbasica.navigation

// ===== IMPORTS =====
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

import com.example.miappbasica.ui.screen.*  // LoginScreen, RegistroScreen, InicioScreen, RecipeList, RecipeDetail, AddRecipeScreen, etc.


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
            // ===== RUTAS EXISTENTES =====
            composable("login") { LoginScreen(navController) }
            composable("registro") { RegistroScreen(navController) }
            composable("inicio") { InicioScreen(navController) }
            composable("bienvenida") { BienvenidaScreen(navController) }
            composable("perfil") { PerfilScreen(navController) }
            composable("configuracion") { ConfiguracionScreen(navController) }
            composable("acerca") { AcercaDeScreen(navController) }

            // ===== RECETAS =====
            composable("recipe_list") { RecipeList(navController) }


            composable(
                route = "recipe_detail/{name}/{imageUri}",
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("imageUri") { type = NavType.StringType; nullable = true }
                )
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name")
                val imageUri = backStackEntry.arguments?.getString("imageUri")
                if (name != null) {
                    RecipeDetail(recipeName = name, imageUri = imageUri)
                }
            }

            // Formulario para agregar receta
            composable("recipe_add") { AddRecipeScreen(navController) }
        }
    }
}

