package com.example.mealplanb.remote

interface HomeMealView {
    fun MealAddSuccess(meal_id: Int)
    fun MealAddFailure(code: Int,msg: String)
    fun FoodListAddSuccess()
    fun FoodListAddFailure(code: Int,msg: String)
}