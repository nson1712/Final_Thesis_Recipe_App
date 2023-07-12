package com.example.final_thesis_recipe_app.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.final_thesis_recipe_app.R
import com.example.final_thesis_recipe_app.databinding.ActivityMealBinding
import com.example.final_thesis_recipe_app.db.MealDatabase
import com.example.final_thesis_recipe_app.fragment.HomeFragment
import com.example.final_thesis_recipe_app.pojo.Meal
import com.example.final_thesis_recipe_app.viewModel.HomeViewModel
import com.example.final_thesis_recipe_app.viewModel.MealViewModel
import com.example.final_thesis_recipe_app.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String
    private lateinit var mealMvvm: MealViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //tao mot the hien cua MealViewModel
//        mealMvvm = ViewModelProviders.of(this)[MealViewModel::class.java]
        val mealDataBase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDataBase)
        mealMvvm = ViewModelProvider(this, viewModelFactory).get(MealViewModel::class.java)

        //lay thong tin meal vua gui tu putExtra ben Main
        getMealInfomationFromIntent()

        //set anh va ten meal
        setInformationInViews()
        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observerMealDetailsLiveData()

        onYouTubeImageClick()
        onFavoriteClick()
    }


    private fun onFavoriteClick() {
        binding.btnAddToFav.setOnClickListener {
            mealToInsert?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this@MealActivity, "Insert complete", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun onYouTubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }
    private var mealToInsert : Meal?=null

    private fun observerMealDetailsLiveData() {
        mealMvvm.observeMealDetailsLiveData().observe(this
        ) { t ->
            onResponseCase()
            val meal = t
            mealToInsert = meal
            binding.tvCategories.text = "Category: ${meal!!.strCategory}"
            binding.tvArea.text = "Category: ${meal!!.strArea}"
            binding.tvDescription.text = meal.strInstructions

            youtubeLink = meal.strYoutube.toString()
        }
    }

    private fun setInformationInViews() {
        //set anh?
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)
        //set title, mau` title cho collapsingToolBar
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInfomationFromIntent() {
        //get cac thong tin da put vao intent
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!


    }

    private fun loadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddToFav.visibility = View.INVISIBLE
        binding.tvInstruction.visibility = View.INVISIBLE
        binding.tvDescription.visibility = View.INVISIBLE
        binding.tvCategories.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE

    }

    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.tvInstruction.visibility = View.VISIBLE
        binding.tvDescription.visibility = View.VISIBLE
        binding.tvCategories.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE

    }


}