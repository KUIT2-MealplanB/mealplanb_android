package com.example.mealplanb

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.databinding.FragmentMenuRecommendHowMenuBinding
import com.example.mealplanb.remote.AuthService
import com.example.mealplanb.remote.FoodSearchResponse
import com.example.mealplanb.remote.RecommendMealAmountView
import com.example.mealplanb.remote.RecommendedMeal
import com.example.mealplanb.remote.SearchFoodView
import com.google.gson.Gson

class MenuRecommendHowMenuFragment : Fragment(), SearchFoodView, RecommendMealAmountView {
    lateinit var binding : FragmentMenuRecommendHowMenuBinding
    lateinit var adapter: MenuRecommendHowMenuAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuRecommendHowMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authService = AuthService(requireContext())
        authService.setSearchFoodView(this)

        authService.searchfood("", 0) { response ->
            if (!isAdded || response == null) {
                return@searchfood
            }

            Log.d("보낸 쿼리", "")

            when (response.code) {
                1000 -> {
                    val searchFoodResponse = response.result
                    SearchFoodSuccess(searchFoodResponse)
                }
                else -> {
                    Log.d("searchFood get error", response.toString())
                }
            }
        }

        binding.menuRecommHowMenuBackbtnCv.setOnClickListener {
            val initMenuFragment = MenuRecommendInitFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(com.example.mealplanb.R.id.menu_recomm_button_cv, initMenuFragment)
                .commit()
        }
        binding.menuRecommHowMenuSearchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                authService.searchfood(s.toString(), 0) { response ->
                    if (!isAdded || response == null) {
                        return@searchfood
                    }

                    Log.d("보낸 쿼리", s.toString())

                    when (response.code) {
                        1000 -> {
                            val searchFoodResponse = response.result
                            SearchFoodSuccess(searchFoodResponse)
                        }
                        else -> {
                            Log.d("searchFood get error", response.toString())
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun SearchFoodSuccess(foodSearchResponse: FoodSearchResponse) {
        foodSearchResponse?.foods?.let { foods ->
            for (food in foods) {
                Log.d("search Food Item in fragment", "Food ID: ${food.foodId}, Food Name: ${food.foodName}, Kcal: ${food.kcal}")
            }
        }

        val items = foodSearchResponse.foods

        val authService = AuthService(requireContext())
        authService.setSearchFoodView(this)
        authService.setRecommendMealAmountView(this)

        adapter = MenuRecommendHowMenuAdapter(items) { item ->
            val menuRecommendFragment = parentFragmentManager.findFragmentById(com.example.mealplanb.R.id.main_frm) as? MenuRecommendFragment

            binding.menuRecommHowMenuSearchEt.text = Editable.Factory.getInstance().newEditable(item.foodName)

            authService.recommendMealAmountCheck(item.foodId.toString())

            menuRecommendFragment?.addInitFragmentItems(
                MenuRecommItem.UserItem(item.foodName, "", 2),
//                MenuRecommItem.SystemHowManyItem(item.foodName,300,item.foodName.toInt(),10,6)
            )
        }

        // Set the adapter for the RecyclerView
        binding.menuRecommHowMenuContentRv.adapter = adapter
        binding.menuRecommHowMenuContentRv.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun FoodDetailSuccess(
        name: String,
        quantity: Int,
        kcal: Double,
        carbohydrate: Double,
        protein: Double,
        fat: Double,
        isFavorite: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun RecommendedMealSuccess(recommendMealCheckResponse: List<RecommendedMeal>) {
        TODO("Not yet implemented")
    }

    override fun mealAmountCheckSuccess(
        food_name: String,
        offer: String,
        offer_kcal: Int,
        offer_quantity: Int,
        offer_carbohydrate: Int,
        offer_protein: Int,
        offer_fat: Int,
        remaining_kcal: Int
    ) {
        val menuRecommendFragment = parentFragmentManager.findFragmentById(com.example.mealplanb.R.id.main_frm) as? MenuRecommendFragment

        menuRecommendFragment?.addInitFragmentItems(
            MenuRecommItem.SystemHowManyItem(food_name,remaining_kcal,offer_kcal,offer,6)
        )
    }
}