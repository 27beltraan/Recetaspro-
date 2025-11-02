package com.example.miappbasica.ui.screen

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.miappbasica.ui.RecipeViewModel

@Composable
fun RecipeList(
    navController: NavHostController,
    vm: RecipeViewModel = viewModel()
) {
    val recipes = vm.recipes.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Lista de Recetas", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(12.dp))
        Button(onClick = { navController.navigate("recipe_add") }) {
            Text("Agregar receta")
        }

        Spacer(Modifier.height(12.dp))
        if (recipes.value.isEmpty()) {
            Text("Aún no hay recetas. ¡Agrega la primera!", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn {
                items(recipes.value) { r ->
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {

                            val encodedUri = r.imageUri?.let { Uri.encode(it) } ?: ""
                            navController.navigate("recipe_detail/${r.name}/$encodedUri")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(r.name)
                    }
                }
            }
        }
    }
}

