package com.example.mealplanb.remote

interface PlanView {
    fun PlanCheckSuccess(initial_weight: Double, target_weight: Double, recommended_kcal: Int, diet_type: String,
                         carbohydrate_rate: Int, protein_rate: Int, fat_rate: Int, target_kcal: Int)
}