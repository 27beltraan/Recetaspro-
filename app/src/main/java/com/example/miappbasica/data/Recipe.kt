package com.example.miappbasica.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val ingredients: String,
    val instructions: String,
    val imageUri: String? = null
)
