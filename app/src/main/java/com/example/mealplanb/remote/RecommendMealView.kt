package com.example.mealplanb.remote

interface RecommendMealView {
    fun cheatMealCheckSuccess(cheat_day_food: List<ChatRecommendMeal>)
    fun myFavoriteMealCheckSuccess(
        food_id: Long, name: String, offer: String, offer_quantity: Int,
        offer_carbohydrate: Int, offer_protein: Int, offer_fat: Int)
    fun communityFavoiriteMealCheckSuccess(food_id: Long, name: String, offer: String, offer_quantity: Int,
                                           offer_carbohydrate: Int, offer_protein: Int, offer_fat: Int)
    fun recommendMealRegistSuccess()
    fun recommendMealRegistFailure(code: Int,msg: String)
}