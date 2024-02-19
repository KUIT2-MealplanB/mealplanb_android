package com.example.mealplanb

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.databinding.FragmentMenuRecommendCheatdayBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import java.util.Calendar
import java.util.Date

class MenuRecommendCheatdayFragment : Fragment() {
    lateinit var binding: FragmentMenuRecommendCheatdayBinding
    var items = arrayListOf(
        Meal("크림파스타", 100.0, 200.0, 30.0, 9.0, 12.0),
        Meal("크림리조또", 120.0, 250.0, 14.0, 4.0, 3.0),
        Meal("식빵", 30.0, 70.0, 19.0, 9.0, 3.0),
        Meal("크림스프", 150.0, 180.0, 7.0, 0.0, 1.0),
        Meal("샐러드", 50.0, 80.0,4.0, 1.0, 0.0),
        Meal("김밥", 120.0, 250.0, 14.0, 4.0,3.0),
        Meal("김치볶음밥", 30.0, 70.0, 19.0, 9.0,3.0),
        Meal("볶음밥", 150.0, 180.0, 7.0, 0.0, 1.0),
        Meal("닭가슴살", 50.0, 80.0,4.0, 1.0, 0.0),
        Meal("닭볶음탕", 100.0, 200.0, 30.0, 9.0, 12.0),
        Meal("계란", 120.0, 250.0, 14.0, 4.0,3.0),
        Meal("샐러리", 30.0, 70.0, 19.0, 9.0,3.0),
        Meal("방울토마토", 150.0, 180.0, 7.0, 0.0, 1.0),
        Meal("사과", 50.0, 80.0,4.0, 1.0, 0.0)
    ) // 예시 데이터 무작위 15개
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuRecommendCheatdayBinding.inflate(layoutInflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuRecommendFragment = parentFragmentManager.findFragmentById(R.id.main_frm) as? MenuRecommendFragment

        var randomMenuIdx = (0 until items.size).random()
        var cal = Calendar.getInstance()
        // Add data directly using the activity's public method
        menuRecommendFragment?.addCheatMenuFragmentItems(
            MenuRecommItem.UserCheatItem("치팅데이", "먹고싶은거 아무거나 먹을래요!",4),
            MenuRecommItem.SystemMenuItem(items[randomMenuIdx].meal_name, items[randomMenuIdx].sacc_gram.toInt(), items[randomMenuIdx].protein_gram.toInt(),items[randomMenuIdx].fat_gram.toInt(),5)
        )

        // 클릭 이벤트 설정
        binding.menuRecommCheatdayBtnSelectCl.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            // SharedPreferences.Editor 객체를 얻어서 값을 저장
            val editor = sharedPref.edit()
            editor.putString("recommendMenu",items[randomMenuIdx].meal_name)
            editor.putInt("recommendMenuCal",items[randomMenuIdx].meal_cal.toInt())
            editor.putInt("recommendMenuSacc",items[randomMenuIdx].sacc_gram.toInt())
            editor.putInt("recommendMenuProtein",items[randomMenuIdx].protein_gram.toInt())
            editor.putInt("recommendMenuFat",items[randomMenuIdx].fat_gram.toInt())

            val gson = Gson()
            var newJson = gson.toJson(MealMainInfo(true,1,items[randomMenuIdx].meal_cal,R.drawable.item_hamburger_img))
            editor.putString("MealMainInfo",newJson)
            editor.apply()

            // 클릭 시 WhatMenuFragment로 교체
//            val selectFragment = MenuRecommendSelectFragment()
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.menu_recomm_button_cv, selectFragment)
//                .commit()
            menuRecommendFragment?.addWhatMenuFragmentItems(
                MenuRecommItem.UserItem("이 음식", "으로 먹을래요", 2),
                MenuRecommItem.SystemUpdateMenuItem(items[randomMenuIdx].meal_name!!,7)
            )
            cal.time = Date()
            menuRecommendFragment?.addItemToRecyclerView(
                RecommendMenu(cal,"첫 끼 : " + items[randomMenuIdx].meal_name,
                    items[randomMenuIdx].sacc_gram.toInt(),
                    items[randomMenuIdx].protein_gram.toInt(),
                    items[randomMenuIdx].fat_gram.toInt()))
            val selectFragment = activity?.findViewById<View>(R.id.menu_recomm_button_cv)
            selectFragment?.visibility = View.GONE

            CoroutineScope(Dispatchers.Main).launch {
                delay(5000)
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commit()
            }
        }

        binding.menuRecommCheatdayBtnCl.setOnClickListener {
            var randomMenuIdx = (0 until items.size).random()
            // Add data directly using the activity's public method
            menuRecommendFragment?.addCheatMenuFragmentItems(
                MenuRecommItem.UserItem("다시 골라주세요!", "", 2),
                MenuRecommItem.SystemMenuItem(items[randomMenuIdx].meal_name, items[randomMenuIdx].sacc_gram.toInt(), items[randomMenuIdx].protein_gram.toInt(),items[randomMenuIdx].fat_gram.toInt(),5)
            )
        }

        // 다른 초기화 작업 등을 수행할 수 있음
    }
}