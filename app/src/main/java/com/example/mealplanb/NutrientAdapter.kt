package com.example.mealplanb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.databinding.ItemNutrientBinding

class NutrientAdapter(private val nutrients: List<Nutrient>) : RecyclerView.Adapter<NutrientAdapter.NutrientViewHolder>() {

    class NutrientViewHolder(val binding: ItemNutrientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutrientViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNutrientBinding.inflate(layoutInflater, parent, false)
        return NutrientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NutrientViewHolder, position: Int) {
        val nutrient = nutrients[position]
        holder.binding.nutNameTv.text = nutrient.name
        holder.binding.nutGramTv.text = nutrient.grams
    }

    override fun getItemCount(): Int {
        return nutrients.size
    }
}
