package com.example.mealplanb.remote

interface HomeMealView {
    fun MealAddSuccess(meal_id: Int)
    fun MealAddFailure(code: Int,msg: String)
    fun FoodListAddSuccess()
    fun FoodListAddFailure(code: Int,msg: String)
    fun FoodListCheckSuccess(meal_id: Int, meal_date: String, meal_type: Int, food_list: List<MealFoodResponseFoodList>)
}