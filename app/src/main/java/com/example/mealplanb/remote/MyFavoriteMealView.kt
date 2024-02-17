package com.example.mealplanb.remote

interface MyFavoriteMealView {
    fun myFavoriteMealCheckSuccess(food_id: Long, name: String, offer: String, offer_carbohydrate: Int, offer_protein: Int, offer_fat: Int)
}