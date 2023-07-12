package com.example.final_thesis_recipe_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.final_thesis_recipe_app.db.MealDatabase

class HomeViewModelFactory(private val mealDataBase: MealDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(mealDataBase) as T
    }
}