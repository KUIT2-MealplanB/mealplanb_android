package com.example.mealplanb.remote

import android.health.connect.datatypes.CervicalMucusRecord.CervicalMucusAppearance

interface SignupView {
    fun SignupLoading()
    fun SignupSuccess()
    fun SignupFailure(code: Int,msg: String)
    fun WeightcheckSuccess(weight:Float, date:String)

    fun UserProfileCheckSuccess(date: String,nickname: String,elapsed_days: Int,remaining_kcal: Int,
                                avatar_color: String,avatar_appearance: String,target_kcal: Int,
                                target_carbohydrate: Int,target_protein: Int,target_fat: Int,
                                kcal: Int,carbohydrate: Int,protein: Int,fat: Int, sodium: Int,
                                sugar: Int,saturated_fat: Int,trans_fat: Int,cholesterol: Int)
    fun mealListDayCheckSuccess(meal_date: String,meals: List<MealListDateResponseMeals>)
    fun handleFavoriteFoodResponse(favoriteFoodResponse: FavoriteFoodResponse?)
}

interface SearchFoodView{
    fun SearchFoodSuccess(foodSearchResponse: FoodSearchResponse)
    fun FoodDetailSuccess(name: String, quantity: Int, kcal: Double, carbohydrate: Double,
                          protein: Double, fat: Double, isFavorite: Boolean)
}