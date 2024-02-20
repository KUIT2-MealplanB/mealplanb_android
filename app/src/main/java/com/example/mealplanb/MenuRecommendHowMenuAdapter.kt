package com.example.mealplanb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.databinding.ItemMenuRecommendSearchMenulistBinding

class MenuRecommendHowMenuAdapter(var mealList: ArrayList<Meal>, val onClick: (Meal)->(Unit)) : RecyclerView.Adapter<MenuRecommendHowMenuAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemMenuRecommendSearchMenulistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myMeal : Meal) {
            binding.itemMenuRecommSearchMenuTv.text = myMeal.meal_name

            binding.root.setOnClickListener {
                onClick(myMeal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMenuRecommendSearchMenulistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mealList[position])
    }

    fun updateList(newItems: ArrayList<Meal>) {
        mealList = newItems
        notifyDataSetChanged() // RecyclerView에 데이터가 변경되었음을 알림
    }
}