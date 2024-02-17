package com.example.mealplanb.remote

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    //키 값 지정
    @SerializedName("email") val email : String,
    @SerializedName("password") val password : String,
    @SerializedName("sex") val sex : String,
    @SerializedName("age") val age : Int,
    @SerializedName("height") val height : Int,
    @SerializedName("initial_weight") val initial_weight : Float,
    @SerializedName("target_weight") val target_weight : Float,
    @SerializedName("diet_type") val diet_type : String,
    @SerializedName("avatar_color") val avatar_color : String,
    @SerializedName("nickname") val nickname : String,
)

data class SignupResponse(
    @SerializedName("member_id") val member_id : Int,
    @SerializedName("jwt") val jwt : String,
)

data class LoginRequest(
    @SerializedName("email") val email : String,
    @SerializedName("password") val password : String
)

data class LoginResponse(
    @SerializedName("memberId") val memberId : Int,
    @SerializedName("jwt") val jwt : String
)

data class Plan(
    @SerializedName("initial_weight") val initial_weight: Double,
    @SerializedName("target_weight") val target_weight: Double,
    @SerializedName("recommended_kcal") val recommended_kcal: Int,
    @SerializedName("diet_type") val diet_type: String,
    @SerializedName("carbohydrate_rate") val carbohydrate_rate: Int,
    @SerializedName("protein_rate") val protein_rate: Int,
    @SerializedName("fat_rate") val fat_rate: Int,
    @SerializedName("target_kcal") val target_kcal: Int
)

data class Weight(
    @SerializedName("weight") val weight : Float,
    @SerializedName("date") val date : String,
)

data class ChatRecommendMeal(
    @SerializedName("food_id") val food_id : Long,
    @SerializedName("name") val name : String,
    @SerializedName("offer") val offer : String,
    @SerializedName("offer_carbohydrate") val offer_carbohydrate : Int,
    @SerializedName("offer_protein") val offer_protein : Int,
    @SerializedName("offer_fat") val offer_fat : Int
)