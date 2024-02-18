package com.example.mealplanb.remote

interface SignupView {
    fun SignupLoading()
    fun SignupSuccess()
    fun SignupFailure(code: Int,msg: String)
    fun WeightcheckSuccess(weight:Float, date:String)
    fun handleFavoriteFoodResponse(favoriteFoodResponse: FavoriteFoodResponse?)
}

interface SearchFoodView{
    fun SearchFoodSuccess(foodSearchResponse: FoodSearchResponse)
}
