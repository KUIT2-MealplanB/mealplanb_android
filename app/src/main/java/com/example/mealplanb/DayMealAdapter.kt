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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DayMealAdapter(var dayMealList: ArrayList<MealMainInfo>, private val context: Context) : RecyclerView.Adapter<DayMealAdapter.ViewHolder>() {
    private val numList : ArrayList<String> = arrayListOf("첫","두","세","네","다섯","여섯","일곱","여덟","아홉","열")
    inner class ViewHolder(val binding: ItemDaymealBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myMealMainInfo: MealMainInfo) {
            binding.daymealMealtitleTv.text = myMealMainInfo.meal_type
            binding.daymealMealIv.setImageResource(myMealMainInfo.meal_img)
            binding.daymealCalTv.text = myMealMainInfo.total_cal.toString() + "kcal"

            if(dayMealList[position].total_cal > 0) {
                binding.daymealPlusbtnTv.visibility = View.GONE
                binding.daymealMealIv.visibility = View.VISIBLE
                binding.daymealCalTv.visibility = View.VISIBLE
            } else {
                binding.daymealPlusbtnTv.visibility = View.VISIBLE
                binding.daymealMealIv.visibility = View.GONE
                binding.daymealCalTv.visibility = View.GONE
            }

            binding.daymealMealinfoCv.setOnLongClickListener {
                binding.daymealMealdelIv.visibility = View.VISIBLE
                true
            }

            binding.daymealMealinfoCv.setOnClickListener {
//                updateSharedPreferences(context,myMealMainInfo.meal_type)
                val sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                val gson = Gson()
                val foodListID = "addFoodList" + myMealMainInfo.meal_type.toString()
                var json = sharedPreferences.getString(foodListID,null)
                var addFoodList : ArrayList<Meal> = gson.fromJson(json, object : TypeToken<ArrayList<Meal>>() {}.type) ?: arrayListOf()
                if (addFoodList.size > 0) {
                    (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_frm, AddMealListFragment())?.commit()
                } else {
                    (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_frm, SearchMealFragment())?.commit()
                }
            }

            binding.daymealMealdelIv.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Remove item from the list
                    dayMealList.removeAt(position)
                    // Notify RecyclerView
                    notifyItemRemoved(position)

                    // Update SharedPreferences
                    val sharedPref = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                    val gson = Gson()
                    val editor = sharedPref.edit()
                    val newJson = gson.toJson(dayMealList)
                    editor.putString("dayMealList",newJson)
                    editor.apply()
                }
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
//            Log.d("logcat",item.total_cal.toString()+"끼니")
            sum += item.total_cal.toInt()
        }
        return sum
    }

    private fun updateSharedPreferences(context: Context, index: Int) {
        val sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("selectedMealNum", index)
        editor.apply()
    }
}