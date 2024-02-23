package com.example.mealplanb.remote

interface AddMealView {
    fun foodAddSuccess(food_id: Int, name: String, category: String, key_nutrient: String, quantity: Int,
                       kcal: Double, carbohydrate: Double, protein: Double, fat: Double, sugar: Double,
                       sodium: Double, cholesterol: Double, saturated_fatty_acid: Double, trans_fatty_acid: Double)
    fun foodAddFailure(code: Int, message: String)
}