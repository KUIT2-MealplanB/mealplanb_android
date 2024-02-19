package com.example.mealplanb

import java.io.Serializable

data class MealMainInfo(
    var meal_active : Boolean,
    var meal_no : Int,
    var meal_id : Int,
    var meal_type : String,
    var total_cal : Double,
    var meal_img : Int,
    val date: String, // 추가된 부분: 날짜 정보를 저장하는 속성
    val weight: Double // 추가된 부분: 무게 정보를 저장하는 속성
) : Serializable