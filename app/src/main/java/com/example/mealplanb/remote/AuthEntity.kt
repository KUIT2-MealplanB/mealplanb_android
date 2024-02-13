package com.example.mealplanb.remote

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email : String,
    @SerializedName("password") val password : String
)

data class LoginResponse(
    @SerializedName("memberId") val memberId : Int,
    @SerializedName("jwt") val jwt : String
)