package com.example.mealplanb

data class MenuRecommDateData(
    val menu_recomm_month : Int,
    val menu_recomm_day : Int,
    val menu_recomm_week : String,
    val type: Int // Default viewType for DateData
)