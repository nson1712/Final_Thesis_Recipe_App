package com.example.final_thesis_recipe_app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.final_thesis_recipe_app.R
import com.example.final_thesis_recipe_app.adapter.CategoriesAdapter
import com.example.final_thesis_recipe_app.adapter.CategoryMealsAdapter
import com.example.final_thesis_recipe_app.databinding.ActivityCategoryMealsBinding
import com.example.final_thesis_recipe_app.fragment.HomeFragment
import com.example.final_thesis_recipe_app.pojo.Meal
import com.example.final_thesis_recipe_app.pojo.MealsByCategory
import com.example.final_thesis_recipe_app.viewModel.CategoryMealsViewModel
import com.example.final_thesis_recipe_app.viewModel.HomeViewModel

class CategoryMealsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryMealsBinding
    lateinit var categoryMealsViewModel: CategoryMealsViewModel
    lateinit var categoryMealsAdapter: CategoryMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()
        categoryMealsViewModel = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]
        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer {mealList->
            categoryMealsAdapter.setMealsList(mealList as ArrayList<MealsByCategory>)

        })

        onItemClick()



    }

    private fun onItemClick() {
        categoryMealsAdapter.onItemClick = {meal ->
            val intent = Intent(this, MealActivity::class.java)
            intent.apply {
                putExtra(HomeFragment.MEAL_ID, meal.idMeal)
                putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
                putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            }
            startActivity(intent)
        }
    }


    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }

    }
}