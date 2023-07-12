package com.example.final_thesis_recipe_app.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_thesis_recipe_app.db.MealDatabase
import com.example.final_thesis_recipe_app.pojo.Category
import com.example.final_thesis_recipe_app.pojo.CategoryList
import com.example.final_thesis_recipe_app.pojo.MealsByCategoryList
import com.example.final_thesis_recipe_app.pojo.MealsByCategory
import com.example.final_thesis_recipe_app.pojo.Meal
import com.example.final_thesis_recipe_app.pojo.MealList
import com.example.final_thesis_recipe_app.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDatabase: MealDatabase): ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()  
    private var popularItemsLiveData = MutableLiveData<ArrayList<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<ArrayList<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private var mealSearchLiveData = MutableLiveData<List<Meal>>()

    private var saveStateRandomMeal: Meal?=null
    fun getRandomMeal(){

        saveStateRandomMeal?.let {meal->
            randomMealLiveData.postValue(meal)
            return
        }
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            //connect api thanh cong
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    saveStateRandomMeal = randomMeal
//                    Log.d("TEST", "id: ${randomMeal.idMeal} name: ${randomMeal.strMeal} ")
                }else{
                    return
                }
            }

            //connect api fail
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())

            }

        })
    }

    fun getMealById(id: String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let{ meal ->
                    bottomSheetMealLiveData.value = meal

                }


            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })



    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if(response.body() != null){
//                    val popularItems: CategoryMeals = response.body()!!.meals[0]
//                    popularItemsLiveData.value = arrayOf(popularItems)

                    val popularItems: ArrayList<MealsByCategory> = response.body()!!.meals
                    popularItemsLiveData.value = popularItems

                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("PopularItem", t.message.toString())
            }
        })
    }

    fun searchMeals(mealSearch: String){
        RetrofitInstance.api.searchMeals(mealSearch).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealList = response.body()?.meals
                mealList.let {
                    mealSearchLiveData.value = mealList
                }

            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModelSearch", t.message.toString())
            }

        })
    }



    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
//                if(response.body() != null){
//                    val category : ArrayList<Category> = response.body()!!.categories
//                    categoriesLiveData.value = category
//                }

                response.body()?.let { categoryList ->
                    categoriesLiveData.value = categoryList.categories
                }
//
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }


        })
    }
    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().update(meal)
        }
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }



    fun observePopularItemsLiveData(): MutableLiveData<ArrayList<MealsByCategory>> {
        return popularItemsLiveData

    }

    fun observeRandomMealLiveData(): LiveData<Meal>{
        return randomMealLiveData
    }

    fun observeCategoriesLiveData(): LiveData<ArrayList<Category>>{
        return categoriesLiveData
    }

    fun observeFavoritesMealsLiveData(): LiveData<List<Meal>>{
        return favoritesMealsLiveData
    }

    fun observeBottomSheetMealLiveData(): LiveData<Meal>{
        return bottomSheetMealLiveData
    }

    fun observeMealSearchLiveData(): LiveData<List<Meal>>{
        return mealSearchLiveData
    }

}