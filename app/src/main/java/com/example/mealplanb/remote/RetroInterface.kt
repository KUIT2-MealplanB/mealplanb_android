package com.example.mealplanb.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface RetroInterface {
    //API 내용 정의 해주어야 함
    @POST("user/signup") //요청 방식
    fun signup(
        @Body request: SignupRequest    //내부에 포함할 데이터
    ): Call<BaseResponse<SignupResponse>>   //반환할 데이터

    //Weight
    @GET("weight")
    fun weightcheck(): Call<BaseResponse<Weight>>
    @POST("weight")
    fun weightpost(
        @Body request : Weight
    ):Call<BaseResponse<Weight>>
    @PATCH("weight")
    fun weightpatch(
        @Body request : Weight
    ):Call<BaseResponse<Weight>>
}