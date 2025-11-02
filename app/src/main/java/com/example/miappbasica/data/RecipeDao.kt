package com.example.miappbasica.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert
    suspend fun insert(recipe: Recipe)

    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAll(): Flow<List<Recipe>>
}
