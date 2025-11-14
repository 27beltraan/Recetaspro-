package com.example.miappbasica.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun RecetaDetalleScreen(
    navController: NavHostController,
    recetaId: Int
) {
    val context = LocalContext.current
    var receta by remember { mutableStateOf<Receta?>(null) }

    LaunchedEffect(recetaId) {
        val lista = cargarRecetasDesdeAssets(context)
        receta = lista.firstOrNull { it.id == recetaId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de receta") },
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
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (val r = receta) {
                null -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Título y tiempo
                        Text(
                            text = r.titulo,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Tiempo: ${r.tiempo}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Divider()

                        // INGREDIENTES
                        Text(
                            text = "Ingredientes",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        if (r.ingredientes.isEmpty()) {
                            Text("No hay ingredientes cargados.")
                        } else {
                            r.ingredientes.forEach { ing ->
                                Text(text = "• $ing")
                            }
                        }

                        Divider()

                        // PASOS DE PREPARACIÓN
                        Text(
                            text = "Preparación",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        if (r.preparacion.isEmpty()) {
                            Text("No hay pasos de preparación cargados.")
                        } else {
                            r.preparacion.forEachIndexed { index, paso ->
                                Text(text = "${index + 1}. $paso")
                            }
                        }
                    }
                }
            }
        }
    }
}
