package com.example.mealplanb

import java.io.Serializable

data class MealMainInfo(
    var meal_active : Boolean,
    var meal_no : Int,
    var total_cal : Double,
    var meal_img : Int
) : Serializable