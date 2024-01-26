package com.example.mealplanb

data class MenuRecommSystemData(
    val menu_recomm_bold : String,
    val menu_recomm_regural : String,
    val type: Int // Default viewType for system items
) {
}