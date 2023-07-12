package com.example.final_thesis_recipe_app.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.final_thesis_recipe_app.pojo.MealsByCategory
import com.example.final_thesis_recipe_app.pojo.MealsByCategoryList
import com.example.final_thesis_recipe_app.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel: ViewModel() {
    val mealsLiveData = MutableLiveData<MutableList<MealsByCategory>>()

    fun getMealsByCategory(categoryName: String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let {mealList->
                    mealsLiveData.value = mealList.meals

                }

            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("CategoryMealsViewModel", t.message.toString())

            }
        })
    }

    fun observeMealsLiveData(): LiveData<MutableList<MealsByCategory>>{
        return mealsLiveData
    }

}