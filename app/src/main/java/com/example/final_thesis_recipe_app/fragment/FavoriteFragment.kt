package com.example.final_thesis_recipe_app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.final_thesis_recipe_app.activities.MainActivity
import com.example.final_thesis_recipe_app.adapter.MealAdapter
import com.example.final_thesis_recipe_app.databinding.FragmentFavoriteBinding
import com.example.final_thesis_recipe_app.pojo.Meal
import com.example.final_thesis_recipe_app.pojo.MealList
import com.example.final_thesis_recipe_app.viewModel.HomeViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoritesAdapter: MealAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        observeFavorites()
        val tempList = mutableListOf<Meal>()


        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deleteMeal = favoritesAdapter.differ.currentList[position]
                tempList.add(deleteMeal)
                viewModel.deleteMeal(deleteMeal)


                val snackbar = Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG)
                snackbar.setAction("Undo", View.OnClickListener {

                    viewModel.insertMeal(deleteMeal)
                })
                snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>(){
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        tempList.remove(deleteMeal)

                    }

                }).show()
            }

        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites) // ????
    }



    private fun prepareRecyclerView() {
        favoritesAdapter = MealAdapter()
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesAdapter
        }
    }


    private fun observeFavorites(){
        viewModel.observeFavoritesMealsLiveData().observe(viewLifecycleOwner, Observer{mealsAdd->
           favoritesAdapter.differ.submitList(mealsAdd) //so sanh dsach cu va moi, DiffUtil tinh toan su thay doi va gui thong bao cap nhat den Adapter
        })

    }


    }
