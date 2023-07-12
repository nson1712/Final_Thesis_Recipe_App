package com.example.final_thesis_recipe_app.fragment

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.final_thesis_recipe_app.R
import com.example.final_thesis_recipe_app.activities.MainActivity
import com.example.final_thesis_recipe_app.adapter.MealAdapter
import com.example.final_thesis_recipe_app.databinding.FragmentSearchBinding
import com.example.final_thesis_recipe_app.viewModel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRecyclerViewAdapter: MealAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        binding.seachArrow.setOnClickListener { searchMeals() }

        observeSearchMealsLiveData()

        //sdung coroutine
//        var searchJob: Job?=null
//        binding.edtSearchBox.addTextChangedListener { searchQuery->
//            searchJob?.cancel()
//            searchJob = lifecycleScope.launch{
//                delay(500)
//                viewModel.searchMeals(searchQuery.toString())
//            }
//        }
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        binding.edtSearchBox.addTextChangedListener { searchQuery->
            scope.launch {
                delay(1000)
                viewModel.searchMeals(searchQuery.toString())
            }

        }
//        binding.edtSearchBox.addTextChangedListener { object : TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                scope.launch {
//                    delay(1000)
//                    viewModel.searchMeals(s.toString())
//                }
//
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//        } }

    }


    private fun observeSearchMealsLiveData() {
        //dang ky 1 Observer quan sat Livedata, tra ve mealList va cap nhat Adapter voi mealList vua tra ve
        viewModel.observeMealSearchLiveData().observe(viewLifecycleOwner, Observer{mealList ->
            searchRecyclerViewAdapter.differ.submitList(mealList)
        })
    }

    private fun searchMeals() {
        val searchQuery = binding.edtSearchBox.text.toString()
        if(searchQuery.isNotEmpty()){
            viewModel.searchMeals(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter = MealAdapter()
        binding.rvSearchMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchRecyclerViewAdapter



        }
    }

}