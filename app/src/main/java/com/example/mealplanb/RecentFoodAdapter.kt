package com.example.mealplanb

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.databinding.ItemMeallistBinding
import com.example.mealplanb.remote.RecommendedMeal
import com.google.gson.Gson

class RecentFoodAdapter(var mealList: ArrayList<RecommendedMeal>, val onClick: (RecommendedMeal)->(Unit)) : RecyclerView.Adapter<RecentFoodAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemMeallistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myMeal: RecommendedMeal) {
            binding.itemMeallistTitleTv.text = myMeal.food_name
            binding.itemMeallistSubtitleTv.text = "100g · " + myMeal.kcal.toString() + "kcal"

            binding.root.setOnClickListener {
                onClick(myMeal)
            }

            binding.itemMeallistDelbtnTv.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mealList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyDataSetChanged() // 변경사항을 알립니다

                    // SharedPreferences에서 데이터 삭제
                    val sharedPreferences =
                        itemView.context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    val gson = Gson()

                    val json = gson.toJson(mealList)
                    editor.putString("oftenFoodList", json)
                    editor.apply()
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMeallistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mealList[position])
    }

    override fun getItemCount(): Int {
        return mealList.size
    }
}