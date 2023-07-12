package com.example.final_thesis_recipe_app.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.final_thesis_recipe_app.db.MealDatabase

//sử dụng class này để tạo ra các đối tượng MealViewModel trong MVVM
class MealViewModelFactory(private val mealDataBase: MealDatabase): ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(mealDataBase) as T
    }
}