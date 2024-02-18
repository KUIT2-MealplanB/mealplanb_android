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

data class Weight(
    @SerializedName("weight") val weight : Float,
    @SerializedName("date") val date : String,
)

data class FavoriteFoodRequest(
    @SerializedName("food_id") val food_id : Int,
)

data class FavoriteFoodResponse(
    @SerializedName("foods") val foods: List<Food>
)

data class Food(
    @SerializedName("food_id") val foodId: Int,
    @SerializedName("food_name") val foodName: String,
    @SerializedName("kcal") val kcal: Int,
)

data class FoodSearchResponse(
    @SerializedName("current_page") val current_page : Int,
    @SerializedName("last_page") val last_page : Int,
    @SerializedName("foods") val foods: List<Food>,
    @SerializedName("member_created") val member_created: Boolean

)