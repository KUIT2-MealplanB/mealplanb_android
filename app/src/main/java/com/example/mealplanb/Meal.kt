package com.example.mealplanb

import java.io.Serializable

data class Meal(
    val meal_name : String,
    var meal_weight : Double,
    var meal_cal : Double,
    var sacc_gram : Double,
    var protein_gram : Double,
    var fat_gram : Double
) : Serializable