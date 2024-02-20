package com.example.mealplanb.remote

interface StatView {
    fun StatPlanCheckSuccess(initial_weight: Double,target_weight: Double,diet_type: String)
}