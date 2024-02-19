package com.example.mealplanb.remote

import android.health.connect.datatypes.CervicalMucusRecord.CervicalMucusAppearance

interface SignupView {
    fun SignupLoading()
    fun SignupSuccess()
    fun SignupFailure(code: Int,msg: String)
    fun WeightcheckSuccess(weight:Float, date:String)
<<<<<<< HEAD
=======

    fun UserProfileCheckSuccess(date: String,nickname: String,elapsed_days: Int,remaining_kcal: Int,
                                avatar_color: String,avatar_appearance: String,target_kcal: Int,
                                target_carbohydrate: Int,target_protein: Int,target_fat: Int,
                                kcal: Int,carbohydrate: Int,protein: Int,fat: Int, sodium: Int,
                                sugar: Int,saturated_fat: Int,trans_fat: Int,cholesterol: Int)
    fun mealListDayCheckSuccess(meal_date: String,meals: List<MealListDateResponseMeals>)
>>>>>>> c3786156ef54f245d1fedb595a41b4ca0212ff47
    fun handleFavoriteFoodResponse(favoriteFoodResponse: FavoriteFoodResponse?)
}

interface SearchFoodView{
    fun SearchFoodSuccess(foodSearchResponse: FoodSearchResponse)
}