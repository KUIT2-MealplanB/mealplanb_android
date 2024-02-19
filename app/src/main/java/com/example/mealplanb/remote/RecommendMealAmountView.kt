package com.example.mealplanb.remote

interface RecommendMealAmountView {
    fun mealAmountCheckSuccess(food_name: String, offer: String, offer_kcal: Int, offer_quantity: Int,
                               offer_carbohydrate: Int, offer_protein: Int, offer_fat: Int, remaining_kcal: Int)
}