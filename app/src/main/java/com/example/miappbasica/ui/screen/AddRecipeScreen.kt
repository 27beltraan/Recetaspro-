package com.example.miappbasica.ui.screen

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.miappbasica.ui.RecipeViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddRecipeScreen(
    navController: NavHostController,
    vm: RecipeViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var pickedImageUri by remember { mutableStateOf<Uri?>(null) }

    var nameErr by remember { mutableStateOf<String?>(null) }
    var ingErr by remember { mutableStateOf<String?>(null) }
    var insErr by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    // --- Galería (Photo Picker)
    val galleryPicker = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        pickedImageUri = uri
    }

    // --- Cámara (TakePicture)
    var cameraTempUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(TakePicture()) { success ->
        if (success) pickedImageUri = cameraTempUri
    }

    fun createImageUri(ctx: Context): Uri {
        val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            .format(System.currentTimeMillis())
        val imagesDir = File(ctx.cacheDir, "images").apply { mkdirs() }
        val file = File(imagesDir, "IMG_$time.jpg")
        val authority = "${ctx.packageName}.fileprovider" // ← sin BuildConfig
        return FileProvider.getUriForFile(ctx, authority, file)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Nueva receta", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(12.dp))
        TextField(
            value = name,
            onValueChange = { name = it; nameErr = if (it.isBlank()) "Ingresa un nombre" else null },
            label = { Text("Nombre") },
            isError = nameErr != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (nameErr != null) Text(nameErr!!, color = MaterialTheme.colorScheme.error)

        Spacer(Modifier.height(12.dp))
        TextField(
            value = ingredients,
            onValueChange = { ingredients = it; ingErr = if (it.isBlank()) "Ingresa ingredientes" else null },
            label = { Text("Ingredientes") },
            isError = ingErr != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (ingErr != null) Text(ingErr!!, color = MaterialTheme.colorScheme.error)

        Spacer(Modifier.height(12.dp))
        TextField(
            value = instructions,
            onValueChange = { instructions = it; insErr = if (it.isBlank()) "Ingresa instrucciones" else null },
            label = { Text("Instrucciones") },
            isError = insErr != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (insErr != null) Text(insErr!!, color = MaterialTheme.colorScheme.error)

        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { galleryPicker.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) },
                modifier = Modifier.weight(1f)
            ) { Text(if (pickedImageUri == null) "Galería" else "Cambiar (galería)") }

            OutlinedButton(
                onClick = {
                    cameraTempUri = createImageUri(context)
                    cameraLauncher.launch(cameraTempUri)
                },
                modifier = Modifier.weight(1f)
            ) { Text("Cámara") }
        }

        if (pickedImageUri != null) {
            Spacer(Modifier.height(12.dp))
            Image(
                painter = rememberAsyncImagePainter(pickedImageUri),
                contentDescription = "Foto de la receta",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                nameErr = if (name.isBlank()) "Ingresa un nombre" else null
                ingErr = if (ingredients.isBlank()) "Ingresa ingredientes" else null
                insErr = if (instructions.isBlank()) "Ingresa instrucciones" else null

                val ok = listOf(nameErr, ingErr, insErr).all { it == null }
                if (ok) {
                    vm.add(
                        name = name,
                        ingredients = ingredients,
                        instructions = instructions,
                        imageUri = pickedImageUri?.toString()
                    ) { navController.popBackStack() }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Guardar") }
    }
}




