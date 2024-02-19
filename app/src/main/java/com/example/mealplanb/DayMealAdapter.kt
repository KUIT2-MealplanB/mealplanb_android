package com.example.mealplanb

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.databinding.ItemDaymealBinding

class DayMealAdapter(var dayMealList: ArrayList<MealMainInfo>, private val context: Context) : RecyclerView.Adapter<DayMealAdapter.ViewHolder>() {
    private val numList : ArrayList<String> = arrayListOf("첫","두","세","네","다섯","여섯","일곱","여덟","아홉","열")
    inner class ViewHolder(val binding: ItemDaymealBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myMealMainInfo: MealMainInfo) {
            binding.daymealMealtitleTv.text = numList[myMealMainInfo.meal_no - 1] + " 끼"
            binding.daymealMealIv.setImageResource(myMealMainInfo.meal_img)
            binding.daymealCalTv.text = myMealMainInfo.total_cal.toString() + "kcal"

            if(dayMealList[position].meal_active) {
                binding.daymealPlusbtnTv.visibility = View.INVISIBLE
                binding.daymealMealIv.visibility = View.VISIBLE
                binding.daymealCalTv.visibility = View.VISIBLE
            } else {
                binding.daymealPlusbtnTv.visibility = View.VISIBLE
                binding.daymealMealIv.visibility = View.INVISIBLE
                binding.daymealCalTv.visibility = View.INVISIBLE
            }

            binding.daymealMealinfoCv.setOnClickListener {
                (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_frm, SearchMealFragment())?.commit()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDaymealBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dayMealList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dayMealList[position])
    }

    fun totCal() : Int {
        var sum = 0
        for(item in dayMealList) {
            Log.d("logcat",item.total_cal.toString()+"끼니")
            sum += item.total_cal.toInt()
        }
        return sum
    }
}