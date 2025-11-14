package com.example.miappbasica.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Receta(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val tiempo: String,
    val categoria: String? = null,
    val dificultad: String? = null,
    val ingredientes: List<String> = emptyList(),
    val preparacion: List<String> = emptyList()
)

fun cargarRecetasDesdeAssets(context: Context): List<Receta> {
    val json = context.assets.open("recetas.json")
        .bufferedReader()
        .use { it.readText() }

    val tipoLista = object : TypeToken<List<Receta>>() {}.type
    return Gson().fromJson(json, tipoLista)
}


