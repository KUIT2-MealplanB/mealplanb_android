package com.example.mealplanb

import java.io.Serializable

data class FoodItem(
    var name: String,
    var saccSize: Double,
    var proteinSize: Double,
    var fatSize: Double,
    var kcal: Double
) : Serializable