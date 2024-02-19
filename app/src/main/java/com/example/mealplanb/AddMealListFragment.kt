package com.example.mealplanb

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.FragmentAddMealListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddMealListFragment : Fragment() {
    lateinit var binding : FragmentAddMealListBinding
    lateinit var mealList : ArrayList<Meal>
    private var addFoodList : ArrayList<Meal> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddMealListBinding.inflate(layoutInflater)
        mealList = arrayListOf()
//        mealList.addAll(
//            arrayListOf(
//                Meal("크림파스타", 100, 200, 30, 9, 12),
//                Meal("크림리조또", 120, 250, 14, 4,3),
//                Meal("식빵", 30, 70, 49, 9,3),
//                Meal("크림스프", 150, 180, 7, 0, 1),
//                Meal("샐러드", 50, 80,4, 1, 0)))

        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs", MODE_PRIVATE)
        val gson = Gson()
        var json = sharedPreferences.getString("addFoodList",null)
        addFoodList = gson.fromJson(json, object : TypeToken<ArrayList<Meal>>() {}.type) ?: arrayListOf()

        for(item in addFoodList) {
            mealList.add(
                Meal(
                    item.meal_name,
                    item.meal_weight.toDouble(),
                    item.meal_cal.toDouble(),
                    item.sacc_gram.toDouble(),
                    item.protein_gram.toDouble(),
                    item.fat_gram.toDouble()
                )
            )
        }

        binding.meallistRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.meallistRv.adapter = AddMealListAdapter(mealList)

        binding.addmeallistBookmarkIv.setOnClickListener {
            binding.addmeallistBookmarkIv.setImageResource(R.drawable.star_full_ic)

            json = sharedPreferences.getString("myMadeList",null)

            val myMadeList: ArrayList<Meal> = gson.fromJson(json, object : TypeToken<ArrayList<Meal>>() {}.type) ?: arrayListOf()

            for(meal in mealList) {
                myMadeList.add(meal)
            }

            val editor = sharedPreferences.edit()
            val newJson = gson.toJson(myMadeList)
            editor.putString("myMadeList", newJson)
            editor.apply()
        }
        binding.addmeallistAddmoreCv.setOnClickListener {
            val editor = sharedPreferences.edit()
            val newJson = gson.toJson(addFoodList)
            editor.putString("addFoodList",newJson)
            editor.apply()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, SearchMealFragment()).commit()
        }
        binding.addmeallistEndCv.setOnClickListener {
//            val intent = Intent(applicationContext,MainActivity::class.java)
            val sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs", MODE_PRIVATE)
            val gson = Gson()
            var tot_cal = 0.0

            for(i in mealList) {
                tot_cal += i.meal_cal
            }

            val editor = sharedPreferences.edit()
            val json = MealMainInfo(true,1,tot_cal,R.drawable.item_hamburger_img)
            var newJson = gson.toJson(json)
            editor.putString("MealMainInfo",newJson)
            editor.apply()
//            intent.putExtra("MealMainInfo",MealMainInfo(true,1,tot_cal,R.drawable.item_hamburger_img))
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commit()
        }
        binding.addmeallistBackmenuCl.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, FoodDetailFragment()).commit()
        }

        return binding.root
    }
}