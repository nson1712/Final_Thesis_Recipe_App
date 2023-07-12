package com.example.final_thesis_recipe_app.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

//    late init var api:MealApi
//    init {
//
//    }

    //by lazy: bien chi duoc khoi tao khi duoc su dung lan dau tien va se giu gia tri sau do
    val api: MealApi by lazy {
        //tao doi tuong retrofit de lam viec voi API cua themealdb.com
        //tao doi tuong Retrofit.Builder voi baseUrl nhu phia duoi
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                //chuyen doi du lieu JSON nhan duoc tu API thanh object
            .addConverterFactory(GsonConverterFactory.create())
            .build()
                //tao 1 the hien cua interface MealApi
            .create(MealApi::class.java)
    }
}