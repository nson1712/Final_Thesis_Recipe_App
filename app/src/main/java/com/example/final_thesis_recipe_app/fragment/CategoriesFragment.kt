package com.example.final_thesis_recipe_app.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.final_thesis_recipe_app.R
import com.example.final_thesis_recipe_app.activities.CategoryMealsActivity
import com.example.final_thesis_recipe_app.activities.MainActivity
import com.example.final_thesis_recipe_app.activities.MealActivity
import com.example.final_thesis_recipe_app.adapter.CategoriesAdapter
import com.example.final_thesis_recipe_app.databinding.FragmentCategoriesBinding
import com.example.final_thesis_recipe_app.viewModel.HomeViewModel

class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observerCategories()
        onCategoryClick()
        onCategoryClick()

    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME, category.strCategory)
            startActivity(intent)


        }
    }


    private fun observerCategories() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer {categories ->
            categoriesAdapter.setCategoryList(categories)
        })

    }

    private fun prepareRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }


    }

}