package com.example.final_thesis_recipe_app.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.final_thesis_recipe_app.activities.MealActivity
import com.example.final_thesis_recipe_app.databinding.CategoryItemsBinding
import com.example.final_thesis_recipe_app.databinding.MealItemBinding
import com.example.final_thesis_recipe_app.fragment.HomeFragment
import com.example.final_thesis_recipe_app.pojo.MealsByCategory

class CategoryMealsAdapter(): RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewModel>() {
    private var mealList = ArrayList<MealsByCategory>()
    var onItemClick: ((MealsByCategory) -> Unit)?=null

    fun setMealsList(mealList: ArrayList<MealsByCategory>){
        this.mealList = mealList
        notifyDataSetChanged()
    }
    class CategoryMealsViewModel (val binding: MealItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewModel {
        return CategoryMealsViewModel(MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: CategoryMealsViewModel, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealList[position].strMeal
        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(mealList[position])
        }
    }


}