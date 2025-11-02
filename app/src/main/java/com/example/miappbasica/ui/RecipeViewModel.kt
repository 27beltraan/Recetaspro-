package com.example.miappbasica.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.miappbasica.data.AppDatabase
import com.example.miappbasica.data.Recipe
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipeViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).recipeDao()

    val recipes = dao.getAll().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun add(
        name: String,
        ingredients: String,
        instructions: String,
        imageUri: String?,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            dao.insert(
                Recipe(
                    name = name.trim(),
                    ingredients = ingredients.trim(),
                    instructions = instructions.trim(),
                    imageUri = imageUri
                )
            )
            onDone()
        }
    }
}


