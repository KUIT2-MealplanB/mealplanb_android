package com.example.mealplanb.remote

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("sex") val sex: String,
    @SerializedName("age") val age: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("initial_weight") val initial_weight: Float,
    @SerializedName("target_weight") val target_weight: Float,
    @SerializedName("diet_type") val diet_type: String,
    @SerializedName("avatar_color") val avatar_color: String,
    @SerializedName("nickname") val nickname: String,
)

data class SignupResponse(
    @SerializedName("member_id") val member_id : Int,
    @SerializedName("jwt") val jwt : String,
)

data class Weight(
    @SerializedName("weight") val weight : Float,
    @SerializedName("date") val date : String,
)