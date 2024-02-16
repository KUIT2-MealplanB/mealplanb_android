package com.example.mealplanb.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetroInterface {
    @POST("user/signup")
    fun signup(
        @Body request: SignupRequest //내부에 포함할 데이터
    ) : Call<BaseResponse<SignupResponse>> //반환할 데이터

    @POST("user/login")
    fun login(
        @Body request: LoginRequest
    ): Call<BaseResponse<LoginResponse>>

    //Weight
    @GET("weight")
    fun weightcheck(): Call<BaseResponse<Weight>>
    @POST("weight")
    fun weightpost(
        @Body request : Weight
    ):Call<BaseResponse<Weight>>
}