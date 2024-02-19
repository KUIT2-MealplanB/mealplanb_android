package com.example.mealplanb

import com.example.mealplanb.remote.ChatMealListResponse

interface AddToRecommendRv {
    fun addItemToRecyclerView(item: ChatMealListResponse)
}