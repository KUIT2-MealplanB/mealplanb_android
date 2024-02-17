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

        val sharedPreferences = requireActivity().getSharedPreferences("myPreferences", MODE_PRIVATE)
        val gson = Gson()
        val selectedMealNum = sharedPreferences.getInt("selectedMealNum",1)
        val foodListID = "addFoodList" + selectedMealNum.toString()
        var json = sharedPreferences.getString(foodListID,null)
        var foodListEmptyFlag : Boolean
        addFoodList = gson.fromJson(json, object : TypeToken<ArrayList<Meal>>() {}.type) ?: arrayListOf()
        if(addFoodList.size > 0) {
            foodListEmptyFlag = false
        } else {
            foodListEmptyFlag = true
        }

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
            editor.putString(foodListID,newJson)
            editor.apply()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, SearchMealFragment()).commit()
        }
        binding.addmeallistEndCv.setOnClickListener {
            var tot_cal = 0.0

            for(i in mealList) {
                tot_cal += i.meal_cal
            }

            val editor = sharedPreferences.edit()

            // 이미지 리스트를 생성하고 랜덤하게 하나를 선택
            val imageList = listOf(R.drawable.item_hamburger_img,
                R.drawable.item_salad_img,
                R.drawable.item_rice_img,
                R.drawable.item_meal_img,
                R.drawable.item_egg_img,
                R.drawable.item_pudding_img,
                R.drawable.item_sugar_img)
            val randomImage = imageList.random()

//            val json = MealMainInfo(true, 1, tot_cal, randomImage)
//            val newJson = gson.toJson(json)
//            editor.putString("MealMainInfo", newJson)
//            editor.apply()

            //home 화면에 표시될 끼니 정보 갱신
            var json = sharedPreferences.getString("dayMealList",null)
            var dayMealList = gson.fromJson(json,object : TypeToken<ArrayList<MealMainInfo>>() {}.type) ?: arrayListOf(
                MealMainInfo(false,1,0.0,0, "", 0.0)
            )
            dayMealList.set(selectedMealNum-1, MealMainInfo(true,selectedMealNum,tot_cal,randomImage,"", 0.0))
            val newJson = gson.toJson(dayMealList)
            editor.putString("dayMealList",newJson)
            editor.apply()

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commit()
        }
        binding.addmeallistBackmenuCl.setOnClickListener {
            if(foodListEmptyFlag) {
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, SearchMealFragment()).commit()
            } else {
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commit()
            }
        }

        return binding.root
    }
}