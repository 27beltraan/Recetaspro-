package com.example.miappbasica.ui.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun RecipeDetail(
    recipeName: String,
    imageUri: String? = null
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Detalles de la Receta", style = MaterialTheme.typography.headlineMedium)

        if (!imageUri.isNullOrBlank()) {
            Spacer(Modifier.height(12.dp))
            Image(
                painter = rememberAsyncImagePainter(Uri.parse(imageUri)),
                contentDescription = "Foto de la receta",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(Modifier.height(12.dp))
        Text("Nombre: $recipeName", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(12.dp))
        Text("Ingredientes:", style = MaterialTheme.typography.titleMedium)
        Text("• …", style = MaterialTheme.typography.bodyLarge)

        Spacer(Modifier.height(12.dp))
        Text("Instrucciones:", style = MaterialTheme.typography.titleMedium)
        Text("1) …", style = MaterialTheme.typography.bodyLarge)
    }
}

