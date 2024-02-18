package com.example.mealplanb.remote

interface RecommendMealListView {
    fun recommendMealListCheckSuccess(recommendMealList: List<ChatMealListResponse>)
}