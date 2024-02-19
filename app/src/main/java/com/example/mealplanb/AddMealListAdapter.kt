package com.example.mealplanb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.databinding.ItemMeallistBinding

class AddMealListAdapter(var mealList: ArrayList<Meal>) : RecyclerView.Adapter<AddMealListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemMeallistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myMeal : Meal) {
            binding.itemMeallistTitleTv.text = myMeal.meal_name
            binding.itemMeallistSubtitleTv.text = myMeal.meal_weight.toString() + "g · " + myMeal.meal_cal.toString() + "kcal"

            binding.itemMeallistDelbtnTv.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    mealList.removeAt(position)
                    notifyItemRemoved(position)
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
}