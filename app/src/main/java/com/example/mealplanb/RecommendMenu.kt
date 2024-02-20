package com.example.mealplanb

import java.util.Calendar

data class RecommendMenu(
    var recommend_date: Calendar,
    var recommend_menu: String,
    var recommend_sacc : Int,
    var recommend_protein : Int,
    var eecommend_fat : Int
)
