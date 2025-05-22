package com.example.lifetaskmanager.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.lifetaskmanager.data.AppDatabase
import com.example.lifetaskmanager.data.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val recipeDao = database.recipeDao()

    val allRecipes: LiveData<List<Recipe>> = recipeDao.getAllRecipes()

    fun insert(name: String, ingredients: String, instructions: String) {
        val recipe = Recipe(
            name = name,
            ingredients = ingredients,
            instructions = instructions
        )
        viewModelScope.launch {
            recipeDao.insert(recipe)
        }
    }

    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipeDao.update(recipe)
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipeDao.delete(recipe)
        }
    }

    fun searchRecipes(query: String): LiveData<List<Recipe>> {
        return recipeDao.searchRecipes(query)
    }
} 