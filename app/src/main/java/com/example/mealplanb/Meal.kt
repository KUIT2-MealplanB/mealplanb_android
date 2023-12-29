package com.example.mealplanb

import java.io.Serializable

data class Meal(
    val meal_name : String,
    val meal_weight : Int,
    val meal_cal : Int
) : Serializable