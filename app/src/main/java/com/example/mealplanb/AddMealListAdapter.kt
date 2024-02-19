package com.example.mealplanb

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.databinding.ItemMeallistBinding
import com.example.mealplanb.remote.MealFoodResponseFoodList
import com.google.gson.Gson


class AddMealListAdapter(var mealList: ArrayList<MealFoodResponseFoodList>) : RecyclerView.Adapter<AddMealListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemMeallistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myMeal : MealFoodResponseFoodList) {
            binding.itemMeallistTitleTv.text = myMeal.name
            binding.itemMeallistSubtitleTv.text = myMeal.quantity.toString() + "g · " + myMeal.kcal.toString() + "kcal"

            binding.itemMeallistDelbtnTv.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Remove item from the list
                    mealList.removeAt(position)
                    // Notify RecyclerView
                    notifyItemRemoved(position)

                    // Update SharedPreferences
                    updateSharedPreferences(binding.root.context)

                    Log.d("asdf", "asdf")

                }
            }
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

    private fun updateSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val selectedMealNum = sharedPreferences.getInt("selectedMealNum",1)
        val foodListID = "addFoodList" + selectedMealNum.toString()
        val newJson = gson.toJson(mealList)
        val editor = sharedPreferences.edit()
        editor.putString(foodListID, newJson)
        editor.apply()
    }
}