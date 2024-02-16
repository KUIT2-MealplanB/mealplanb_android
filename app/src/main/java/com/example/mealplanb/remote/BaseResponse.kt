package com.example.mealplanb.remote

import com.google.gson.annotations.SerializedName

data class BaseResponse <T>(
    @SerializedName("is_sucess") val is_sucess : Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("timestamp") val timestamp : String,
    @SerializedName("result") val result : T, //자료형 명시X 아무거나 들어와도 걍 쓰겠다.
)