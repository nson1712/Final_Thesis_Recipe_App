package com.example.final_thesis_recipe_app.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_thesis_recipe_app.db.MealDatabase
import com.example.final_thesis_recipe_app.pojo.MealsByCategoryList
import com.example.final_thesis_recipe_app.pojo.Meal
import com.example.final_thesis_recipe_app.pojo.MealList
import com.example.final_thesis_recipe_app.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(val mealDataBase: MealDatabase): ViewModel() {
    private var mealDetailsLiveData = MutableLiveData<Meal>()


    fun getMealDetail(id: String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    mealDetailsLiveData.value = response.body()!!.meals[0]
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity", t.message.toString())

            }
        })
    }
    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDataBase.mealDao().update(meal)
        }
    }

    fun observeMealDetailsLiveData(): LiveData<Meal>{
        return mealDetailsLiveData
    }




}