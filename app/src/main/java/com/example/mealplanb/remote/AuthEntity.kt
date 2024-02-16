package com.example.mealplanb.remote

import com.google.gson.annotations.SerializedName

//로그인 request
data class LoginRequest(
    @SerializedName("email") val email : String,
    @SerializedName("password") val password : String
)

//로그인 response
data class LoginResponse(
    @SerializedName("memberId") val memberId : Int,
    @SerializedName("jwt") val jwt : String
)
