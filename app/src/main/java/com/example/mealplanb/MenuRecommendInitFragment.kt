package com.example.mealplanb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.databinding.FragmentMenuRecommendHowEatBinding
import com.example.mealplanb.databinding.FragmentMenuRecommendInitBinding
import com.example.mealplanb.remote.AuthService
import com.example.mealplanb.remote.Food
import com.example.mealplanb.remote.MealListDateResponseMeals
import com.example.mealplanb.remote.SignupView
import java.text.SimpleDateFormat
import java.util.Calendar

class MenuRecommendInitFragment : Fragment(), SignupView {
    lateinit var binding: FragmentMenuRecommendInitBinding
    private var cal : Calendar = Calendar.getInstance()
    private var remain_cal: Int = 0
    private var remain_sacc: Int = 0
    private var remain_protein: Int = 0
    private var remain_fat: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuRecommendInitBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuRecommendFragment = parentFragmentManager.findFragmentById(R.id.main_frm) as? MenuRecommendFragment

        // 클릭 이벤트 설정
        binding.menuRecommInitBtnWhatCl.setOnClickListener {
            // Add data directly using the activity's public method
            val authService = AuthService(requireContext())
            authService.setSignupView(this)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            authService.userProfileCheck(dateFormat.format(cal.time))

//            menuRecommendFragment?.addInitFragmentItems(
//                MenuRecommItem.UserItem("어떤 음식", "을 먹을까요?", 2),
//                MenuRecommItem.SystemKcalItem(remain_cal, remain_sacc,remain_protein,remain_fat, 3)
//            )

//            // 클릭 시 WhatMenuFragment로 교체
//            val whatMenuFragment = MenuRecommendWhatMenuFragment()
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.menu_recomm_button_cv, whatMenuFragment)
//                .commit()
        }

        binding.menuRecommInitBtnHowCl.setOnClickListener {
            val howMenuFragment = MenuRecommendHowMenuFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.menu_recomm_button_cv, howMenuFragment)
                .commit()

            // Add data directly using the activity's public method
            menuRecommendFragment?.addInitFragmentItems(
                MenuRecommItem.UserItem("얼마나", " 먹을까요?", 2),
                MenuRecommItem.SystemItem("어떤 음식","을 먹고 싶으세요?",3)
            )
        }

        // 다른 초기화 작업 등을 수행할 수 있음
    }

    override fun SignupLoading() {
        TODO("Not yet implemented")
    }

    override fun SignupSuccess() {
        TODO("Not yet implemented")
    }

    override fun SignupFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun WeightcheckSuccess(weight: Float, date: String) {
        TODO("Not yet implemented")
    }

    override fun UserProfileCheckSuccess(
        date: String,
        nickname: String,
        elapsed_days: Int,
        remaining_kcal: Int,
        avatar_color: String,
        avatar_appearance: String,
        target_kcal: Int,
        target_carbohydrate: Int,
        target_protein: Int,
        target_fat: Int,
        kcal: Int,
        carbohydrate: Int,
        protein: Int,
        fat: Int,
        sodium: Int,
        sugar: Int,
        saturated_fat: Int,
        trans_fat: Int,
        cholesterol: Int
    ) {
        remain_cal = remaining_kcal
        remain_sacc = target_carbohydrate - carbohydrate
        remain_protein = target_protein - protein
        remain_fat = target_fat - fat

        val menuRecommendFragment = parentFragmentManager.findFragmentById(R.id.main_frm) as? MenuRecommendFragment

        menuRecommendFragment?.addInitFragmentItems(
            MenuRecommItem.UserItem("어떤 음식", "을 먹을까요?", 2),
            MenuRecommItem.SystemKcalItem(remain_cal, remain_sacc,remain_protein,remain_fat, 3)
        )

        // 클릭 시 WhatMenuFragment로 교체
        val whatMenuFragment = MenuRecommendWhatMenuFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.menu_recomm_button_cv, whatMenuFragment)
            .commit()
    }

    override fun mealListDayCheckSuccess(
        meal_date: String,
        meals: List<MealListDateResponseMeals>
    ) {
        TODO("Not yet implemented")
    }

    override fun handleFavoriteFoodResponse(favoriteFoodResponse: List<Food>) {
        TODO("Not yet implemented")
    }
}