package com.example.mealplanb


// StackBarChartAdapter.kt
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.R
import com.example.mealplanb.StackChartItem

class StackBarChartAdapter(private val stackChartItems: List<StackChartItem>) :
    RecyclerView.Adapter<StackBarChartAdapter.StackBarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StackBarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stack_bar, parent, false)
        return StackBarViewHolder(view)
    }

    override fun onBindViewHolder(holder: StackBarViewHolder, position: Int) {
        val chartItem = stackChartItems[position]

        // Set the width of each bar based on the corresponding value
        val fatLayoutParams = holder.fatView.layoutParams
        fatLayoutParams.height = calculateBarHeight(chartItem.fatValue)
        holder.fatView.layoutParams = fatLayoutParams

        val proteinLayoutParams = holder.proteinView.layoutParams
        proteinLayoutParams.height = calculateBarHeight(chartItem.proteinValue)
        holder.proteinView.layoutParams = proteinLayoutParams

        val carboLayoutParams = holder.carboView.layoutParams
        carboLayoutParams.height = calculateBarHeight(chartItem.carboValue)
        holder.carboView.layoutParams = carboLayoutParams

    }
    override fun getItemCount(): Int = stackChartItems.size

    class StackBarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fatView: View = itemView.findViewById(R.id.stack_bar_chart_fat_cv)
        val proteinView: View = itemView.findViewById(R.id.stack_bar_chart_protein_cv)
        val carboView: View = itemView.findViewById(R.id.stack_bar_chart_carbo_cv)
    }

    private fun calculateBarHeight(value: Int): Int {
        try {
            // Adjust this calculation based on your requirements
            return (value * 0.17).toInt() // Example calculation, you may need to adjust the multiplier
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }
}