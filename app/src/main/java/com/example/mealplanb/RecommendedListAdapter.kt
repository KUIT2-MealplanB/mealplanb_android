package com.example.mealplanb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.databinding.ItemMeallistBinding
import com.example.mealplanb.databinding.ItemRecommendedListBinding
import com.example.mealplanb.remote.ChatMealListResponse
import java.util.Calendar

class RecommendedListAdapter(var recomList: ArrayList<ChatMealListResponse>) : RecyclerView.Adapter<RecommendedListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemRecommendedListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myRecom : ChatMealListResponse) {
            binding.itemRecommListTitle.text = myRecom.date
            binding.itemRecommListMenu.text = myRecom.meal_type + " : " + myRecom.name
            binding.itemRecommListSacc.text = myRecom.offer_carbohydrate.toString() + "g"
            binding.itemRecommListProtein.text = myRecom.offer_protein.toString() + "g"
            binding.itemRecommListFat.text = myRecom.offer_fat.toString() + "g"
//            binding.itemMeallistTitleTv.text = myRecom.meal_name
//            binding.itemMeallistSubtitleTv.text = myMeal.meal_weight.toString() + "g · " + myMeal.meal_cal.toString() + "kcal"
//
//            binding.itemMeallistDelbtnTv.setOnClickListener {
//                val position = adapterPosition
//                if(position != RecyclerView.NO_POSITION) {
//                    mealList.removeAt(position)
//                    notifyItemRemoved(position)
//                }
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecommendedListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = recomList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recomList[position])
    }

    fun setDayText(cal: Calendar) : String {
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val dayOfWeek = when(cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "일"
            Calendar.MONDAY -> "월"
            Calendar.TUESDAY -> "화"
            Calendar.WEDNESDAY -> "수"
            Calendar.THURSDAY -> "목"
            Calendar.FRIDAY -> "금"
            Calendar.SATURDAY -> "토"
            else -> ""
        }

        return year.toString() + "." + month.toString() + "." + day.toString() + " " + dayOfWeek + "요일"
    }
}