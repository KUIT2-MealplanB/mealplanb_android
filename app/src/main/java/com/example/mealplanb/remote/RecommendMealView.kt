package com.example.mealplanb.remote

interface RecommendMealView {
    fun myFavoriteMealCheckSuccess(food_id: Long, name: String, offer: String, offer_carbohydrate: Int, offer_protein: Int, offer_fat: Int)
    fun communityFavoiriteMealCheckSuccess(food_id: Long, name: String, offer: String, offer_carbohydrate: Int, offer_protein: Int, offer_fat: Int)
}