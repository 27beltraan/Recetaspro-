package com.example.miappbasica.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.miappbasica.data.Receta
import com.example.miappbasica.data.cargarRecetasDesdeAssets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetasScreen(navController: NavHostController) {
    val context = LocalContext.current

    var recetas by remember { mutableStateOf<List<Receta>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }


    var textoBusqueda by remember { mutableStateOf("") }


    var ordenSeleccionado by remember { mutableStateOf("Nombre") }

    LaunchedEffect(Unit) {
        try {
            recetas = cargarRecetasDesdeAssets(context)
        } catch (e: Exception) {
            error = "Error al cargar recetas: ${e.message}"
        }
    }


    val recetasFiltradasOrdenadas = remember(recetas, textoBusqueda, ordenSeleccionado) {
        var lista = recetas

        if (textoBusqueda.isNotBlank()) {
            val query = textoBusqueda.lowercase()
            lista = lista.filter { r ->
                r.titulo.lowercase().contains(query) ||
                        r.descripcion.lowercase().contains(query)
            }
        }

        lista = when (ordenSeleccionado) {
            "Nombre" -> lista.sortedBy { it.titulo }
            "Tiempo" -> lista.sortedBy { it.tiempo }
            else -> lista
        }

        lista
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recetas Pro+") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        when {
            error != null -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error ?: "Error desconocido",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            recetas.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    OutlinedTextField(
                        value = textoBusqueda,
                        onValueChange = { textoBusqueda = it },
                        label = { Text("Buscar receta") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Ordenar por:")

                        FilterChip(
                            selected = ordenSeleccionado == "Nombre",
                            onClick = { ordenSeleccionado = "Nombre" },
                            label = { Text("Nombre") }
                        )

                        FilterChip(
                            selected = ordenSeleccionado == "Tiempo",
                            onClick = { ordenSeleccionado = "Tiempo" },
                            label = { Text("Tiempo") }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(recetasFiltradasOrdenadas) { receta ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        navController.navigate("receta_detalle/${receta.id}")
                                    },
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = receta.titulo,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = receta.descripcion,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 2
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Tiempo: ${receta.tiempo}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
