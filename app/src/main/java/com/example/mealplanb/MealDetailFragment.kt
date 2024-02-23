package com.example.mealplanb

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.FragmentMealDetailBinding
import com.example.mealplanb.remote.AuthService
import com.example.mealplanb.remote.Food
import com.example.mealplanb.remote.MealListDateResponseMeals
import com.example.mealplanb.remote.MyMealFoodListResponse
import com.example.mealplanb.remote.SignupView
import com.example.mealplanb.remote.mymealResponse
import com.google.gson.Gson

class MealDetailFragment : Fragment(), SignupView {
    lateinit var binding: FragmentMealDetailBinding
    lateinit var mealdetailList: ArrayList<MyMealFoodListResponse>

    lateinit var mymealfoodlistadapter: MyMealFoodListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealDetailBinding.inflate(layoutInflater)

        // 리스트 초기화
        mealdetailList = ArrayList()

        //API관련
        val authService = AuthService(requireContext())
        authService.setSignupView(this)

        val sharedPreferences = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("Key", "")
        Log.d("MealDetailFragment 가져오기", "가져온 내용: $json") // 로그 추가
        val myMeal = gson.fromJson(json, mymealResponse::class.java)
        Log.d("나의 식단 id 확인","${myMeal.favorite_meal_id}")
        authService.getmymealfoodlist(myMeal.favorite_meal_id)

        binding.mealdetaillistRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.mealdetaillistRv.adapter = MyMealFoodListAdapter(mealdetailList)

        binding.mealdetailBackmenuCl.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_frm, SearchMealFragment()).commit()
        }

        return binding.root
    }


    override fun SignupLoading() {
        Toast.makeText(requireContext(), "loading 중", Toast.LENGTH_SHORT).show()
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
        TODO("Not yet implemented")
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

    override fun handleMyMealResponse(myMealResponse: List<mymealResponse>) {
        TODO("Not yet implemented")
    }

    override fun handleMyMealFoodListResponse(myMealFoodListResponse: List<MyMealFoodListResponse>) {
        mealdetailList.clear() // 기존 리스트 초기화
        mealdetailList.addAll(myMealFoodListResponse) // 새로 받아온 리스트를 추가

        mymealfoodlistadapter = MyMealFoodListAdapter(mealdetailList)
        binding.mealdetaillistRv.adapter = mymealfoodlistadapter
    }


}