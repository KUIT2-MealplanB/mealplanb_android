package com.example.mealplanb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.databinding.ItemMeallistBinding
import com.example.mealplanb.remote.MealFoodResponseFoodList
import com.example.mealplanb.remote.MyMealFoodListResponse

class MyMealFoodListAdapter(var mealList: ArrayList<MyMealFoodListResponse>) : RecyclerView.Adapter<MyMealFoodListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemMeallistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myMeal : MyMealFoodListResponse) {
            binding.itemMeallistTitleTv.text = myMeal.food_name
            binding.itemMeallistSubtitleTv.text = myMeal.quantity.toString() + "g · " + myMeal.kcal.toString() + "kcal"
            binding.itemMeallistDelbtnTv.text =""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMeallistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mealList[position])
    }
}